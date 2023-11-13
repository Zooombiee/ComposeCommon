package com.cxc.common.http.interceptor

import com.cxc.common.http.converter.IVUtil
import com.cxc.common.http.converter.IVUtil.toIVParameter
import com.cxc.common.utils.ZKGsonUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.util.*

/**
 * 通用请求头
 */
class YHAppHeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val paramsMap = getAllParamHap(request)

        val ivParams = paramsMap["v"] as String?

        val builder = chain.request().newBuilder()
        // val token = MmkvUtils.getString(ZKConstant.ZKSP.USER_TOKEN)
        val timestamp = (System.currentTimeMillis() / 1000).toString()
        // val sign = ZKEncryptUtils.createSign(getAllParamHap(chain.request()),token,nonceStr,timestamp)

        builder
            // .addHeader("device", Build.MANUFACTURER + " " + Build.MODEL)
            // .addHeader("uuid", ZKDeviceIdFactory.getInstance(ZKBaseApplication.getContext()).deviceUuid)
            // .addHeader("bundle-id", ZKAppUtils.getAppPackageName(ZKBaseApplication.getContext()))
            // .addHeader("version", ZKAppUtils.getVersionName(ZKBaseApplication.getContext()))
            // .addHeader("Authorization", token)
            // .addHeader("Content-Type", "application/json;charset=UTF-8")
            .addHeader("timestamp", timestamp)
            .addHeader("platform", "android")
            .addHeader("Connection", "keep-alive")
            .apply {
               if (ivParams!=null){
                   addHeader("TOKEN",
                       IVUtil.getIVFromCache(ivParams.toIVParameter().getKey())?.second
                           ?: ""
                   )
               }
            }
            .build()

        return chain.proceed(builder.build())
    }

    /**
     * 获取所有参数
     * @param request
     * @return
     */
    private fun getAllParamHap(request: Request): TreeMap<Any, Any?> {
        val requestBody = request.body
        val rootMap = TreeMap<Any, Any?>()
        val httpUrl = request.url
        val method = request.method
        val isGet = method == "GET"
        val isPatch = method == "PATCH"
        val isPost = method == "POST"
        val isPut = method == "PUT"
        val isDelete = method == "DELETE"
        if (isGet || isDelete || isPatch) {
            // 通过请求地址(最初始的请求地址)获取到参数列表
            val parameterNames = httpUrl.queryParameterNames
            for (key in parameterNames) {  // 循环参数列表
                rootMap[key] = httpUrl.queryParameter(key)
            }
        } else if (isPost || isPut) {
            if (requestBody is RequestBody) {
                // buffer流
                val buffer = Buffer()
                try {
                    requestBody.writeTo(buffer)
                    return ZKGsonUtils.jsonToMap(buffer.readUtf8())
                } catch (e: Exception) {
                    buffer.close()
                    e.printStackTrace()
                } finally {
                    buffer.close()
                }
            }
        }
        return rootMap
    }
}