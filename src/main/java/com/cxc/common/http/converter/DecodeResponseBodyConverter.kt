package com.cxc.common.http.converter

import com.cxc.common.http.converter.IVUtil.toIVParameter
import com.cxc.common.log.YLog
import com.cxc.common.utils.AESUtils
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import okio.ByteString.Companion.decodeHex
import retrofit2.Converter

/**
 *@Date: 2023/7/13
 *@Time: 11:31
 *@Author:cxc
 *@Description: 有虎请求解密
 */
class DecodeResponseBodyConverter<T>(val gson: Gson, val adapter: TypeAdapter<T>) :
    Converter<ResponseBody, T> {

    val Tag = "http decode"

    override fun convert(responseBody: ResponseBody): T? {

        val result = responseBody.string()

        val responseDataString = result.toIVParameter()

        val key = responseDataString.getKey()

        val ivParameter = IVUtil.getIVFromCache(key) ?: return null

        val rawData = responseDataString.second.decodeHex().toByteArray()

        val output = AESUtils.base64Encode(rawData)

        IVUtil.removeFormCache(key)

        val json = AESUtils.decrypt(output, ivParameter.toFullString())

        val t = adapter.fromJson(json)
        YLog.d(Tag, "解密结果:$json " )
        return t
    }
}