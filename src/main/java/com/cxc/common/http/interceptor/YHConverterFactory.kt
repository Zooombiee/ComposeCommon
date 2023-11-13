package com.cxc.common.http.interceptor

import com.cxc.common.http.converter.DecodeResponseBodyConverter
import com.cxc.common.http.converter.EncodeRequestBodyConverter
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 *@Date: 2023/7/13
 *@Time: 10:35
 *@Author:cxc
 *@Description: 有虎请求解析类
 */
class YHConverterFactory private constructor(val gson: Gson) : Converter.Factory() {

    private val TAG = "YHConverterFactory"

    companion object {
        fun create(): YHConverterFactory {
            return create(Gson())
        }

        fun create(gson: Gson?): YHConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return YHConverterFactory(gson)
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return DecodeResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return EncodeRequestBodyConverter(gson, adapter)
    }
}