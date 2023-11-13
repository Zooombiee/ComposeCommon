package com.cxc.common.http.interceptor;

import com.cxc.common.utils.ZKGsonUtils;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.TreeMap;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * author : wpt
 * date   : 2021/7/2816:23
 * desc   : 参数拦截器
 */
public class ZKParamsInterceptor implements Interceptor {


    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        request = convertParamsToJson(request);
        Request.Builder builder = request.newBuilder();
        return chain.proceed(builder.build());
    }

    /**
     * 当请求方式是post或者put时，后端需要的参数类型是json格式，将参数转换成json并传递json数据
     *
     * @param request
     * @return
     */
    private Request convertParamsToJson(Request request) {
        RequestBody requestBody = request.body();
        TreeMap<Object, Object> rootMap = new TreeMap<>();
        HttpUrl httpUrl = request.url();
        String method = request.method();
        boolean isPost = method.equals("POST");
        boolean isPut = method.equals("PUT");
        if (isPost || isPut) {
            if (requestBody == null) {
                return request;
            }
            try {
                Buffer buffer = new Buffer();
                String newJsonParams = "";
                requestBody.writeTo(buffer);
                String oldParamsStr = buffer.readUtf8();
                if (requestBody instanceof FormBody) {//RequestBody是FormBody  @Field该方式传参(传递的不是json数据，需要处理)
                    String[] splitAnd = oldParamsStr.split("&");
                    for (String str : splitAnd) {
                        String[] splitEqual = str.split("=");
                        for (int i = 0; i < splitEqual.length; i = i + 2) {
                            //value为空时手动传入一个空串
                            String value = splitEqual.length > i + 1 ? splitEqual[i + 1] : "";
                            rootMap.put(splitEqual[i], value);
//                            if (value.matches("[0-9]+")) {
//                                rootMap.put(splitEqual[i], Integer.parseInt(value));
//                            } else {
//                                rootMap.put(splitEqual[i], value);
//                            }
                        }
                    }
                    newJsonParams = ZKGsonUtils.getGsonInstance().toJson(rootMap);
                } else if (requestBody instanceof MultipartBody) {//RequestBody是MultipartBody(暂不处理)

                } else { //@Body该方式传参（传递的是json数据）
                    newJsonParams = oldParamsStr;
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), newJsonParams);
                request = isPost ? request.newBuilder().url(httpUrl).post(body).build()
                        : request.newBuilder().url(httpUrl).put(body).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return request;
    }


}
