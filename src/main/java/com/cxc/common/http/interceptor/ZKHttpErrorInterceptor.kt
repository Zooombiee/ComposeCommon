package com.cxc.common.http.interceptor

import android.text.TextUtils
import com.cxc.common.http.httpbase.interceptor.ZipHelper
import com.cxc.common.http.httpbase.interceptor.loging.LogInterceptor
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset


/**
 * 拦截器处理code!=200的情况(有些业务逻辑接口，比如token失效也会走code!=200逻辑)
 * 因为服务端除了200000外，其他的都走Http错误逻辑(code!=200)
 */
class ZKHttpErrorInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val response = chain.proceed(builder.build())
        val code = response.code
        if (code != 200) {
            return rebuildResponse(response)
        }
        return response
    }

    private fun rebuildResponse(response: Response): Response {
        val responseBody = response.newBuilder().build().body
        val source = responseBody!!.source()
        source.request(Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer

        //获取content的压缩类型
        val encoding = response
            .headers["Content-Encoding"]
        val clone = buffer.clone()

        //解析response content
        val content = parseContent(responseBody, encoding, clone)

        if (TextUtils.isEmpty(content)) {
            return response
        }

        if (!content!!.startsWith("{") || !content.endsWith("}")) {
            return response
        }

        val newBuilder = Response.Builder()
            .request(response.request)
            .protocol(response.protocol)
            .code(200)
            .message(response.message)
            .body(response.body)

        return newBuilder.build()
    }

    /**
     * 解析服务器响应的内容
     *
     * @param responseBody [ResponseBody]
     * @param encoding     编码类型
     * @param clone        克隆后的服务器响应内容
     * @return 解析后的响应结果
     */
    private fun parseContent(
        responseBody: ResponseBody?,
        encoding: String?,
        clone: Buffer
    ): String? {
        var charset = Charset.forName("UTF-8")
        val contentType = responseBody!!.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset)
        }
        //content 使用 gzip 压缩
        return when {
            "gzip".equals(encoding, ignoreCase = true) -> {
                //解压
                ZipHelper.decompressForGzip(
                    clone.readByteArray(),
                    LogInterceptor.convertCharset(charset)
                )
            }
            "zlib".equals(encoding, ignoreCase = true) -> {
                //content 使用 zlib 压缩
                ZipHelper.decompressToStringForZlib(
                    clone.readByteArray(),
                    LogInterceptor.convertCharset(charset)
                )
            }
            else -> {
                //content 没有被压缩, 或者使用其他未知压缩方式
                clone.readString(charset)
            }
        }
    }
}