package com.cxc.common.http


class YHBaseResponse<T>(
    val code: Int,
    val data: T,
    val error: Any?,
    val message: String,
):BaseResponse<T>() {
    override fun isSuccess() = code == 200

    override fun getResponseData(): T = data

    override fun getResponseCode() = code

    override fun getResponseMsg() = message
}