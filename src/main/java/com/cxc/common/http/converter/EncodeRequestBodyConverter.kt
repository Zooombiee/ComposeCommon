package com.cxc.common.http.converter

import com.cxc.common.log.YLog
import com.cxc.common.utils.AESUtils
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Converter

/**
 *@Date: 2023/7/13
 *@Time: 11:31
 *@Author:cxc
 *@Description:有虎请求加密
 */
class EncodeRequestBodyConverter<T>(val gson: Gson, val adapter: TypeAdapter<T>) :
    Converter<T, RequestBody> {

    val Tag = "http encode"

    override fun convert(value: T): RequestBody? {
        val json = gson.toJson(value)
        YLog.w(Tag,"请求参数 json = $json")

        val ivParameter = IVUtil.getRandomIVParameter()

        val requestJson = JSONObject().apply {
            put(
                "v",
                ivParameter!!.first + AESUtils.encrypt(json.toString(), ivParameter.toFullString()) + ivParameter.third
            )
        }


        return  requestJson.toString().toRequestBody( "".toMediaTypeOrNull())
    }

}