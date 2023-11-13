package com.cxc.common.http.interceptor

import com.cxc.common.utils.ZKGsonUtils
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.util.*

/**
 *@Date: 2023/7/12
 *@Time: 21:49
 *@Author:cxc
 *@Description:
 */
class YHParamsInterceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        request = convertParamsToJson(request)
        val builder: Request.Builder = request.newBuilder()
        return chain.proceed(builder.build())
    }

    /**
     * 当请求方式是post或者put时，后端需要的参数类型是json格式，将参数转换成json并传递json数据
     *
     * @param request
     * @return
     */
    open fun convertParamsToJson(request: Request): Request{
        var request = request
        val requestBody = request.body
        val rootMap = TreeMap<Any, Any>()
        val httpUrl = request.url
        val method = request.method
        val isPost = method == "POST"
        val isPut = method == "PUT"
        if (isPost || isPut) {
            if (requestBody == null) {
                return request
            }
            try {
                val buffer = Buffer()
                var newJsonParams: String? = ""
                requestBody.writeTo(buffer)
                val oldParamsStr = buffer.readUtf8()
                if (requestBody is FormBody) { //RequestBody是FormBody  @Field该方式传参(传递的不是json数据，需要处理)
                    val splitAnd = oldParamsStr.split("&".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    for (str in splitAnd) {
                        val splitEqual = str.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        var i = 0
                        while (i < splitEqual.size) {

                            //value为空时手动传入一个空串
                            val value = if (splitEqual.size > i + 1) splitEqual[i + 1] else ""
                            rootMap[splitEqual[i]] = value
                            i = i + 2
                        }
                    }
                    newJsonParams = ZKGsonUtils.getGsonInstance().toJson(rootMap)
                } else if (requestBody is MultipartBody) { //RequestBody是MultipartBody(暂不处理)
                } else { //@Body该方式传参（传递的是json数据）
                    newJsonParams = oldParamsStr
                }
                val body: RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), newJsonParams!!)
                request = if (isPost) request.newBuilder().url(httpUrl).post(body)
                    .build() else request.newBuilder().url(httpUrl).put(body).build()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return request
    }
}