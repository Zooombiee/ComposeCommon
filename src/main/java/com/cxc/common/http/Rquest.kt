package com.cxc.common.http

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cxc.common.ext.HttpResultCallBack
import com.cxc.common.http.httpbase.exception.AppException
import com.cxc.common.http.httpbase.exception.ExceptionHandle
import com.cxc.common.log.YLog
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



/**
 *@Date: 2023/11/13
 *@Time: 11:25
 *@Author:cxc
 *@Description:
 */

 fun <T> ViewModel.requestApi(
    block: suspend () -> BaseResponse<T>,
    listener: HttpResultCallBack<T>.() -> Unit
) {
    viewModelScope.launch {
        val httpCallBack = HttpResultCallBack<T>().apply { listener() }
        runCatching {
            //请求体
            block()
        }.onSuccess { result ->
            when {
                result.isSuccess() -> {
                    httpCallBack.success?.invoke(result.getResponseData())
                }

                else -> {
                    httpCallBack.failed?.invoke(
                        AppException(
                            result.getResponseCode(),
                            result.getResponseMsg()
                        )
                    )
                }
            }
        }.onFailure { //网络层错误
            YLog.e("网络错误", "e =" + it.stackTraceToString())
            httpCallBack.failed?.invoke(ExceptionHandle.handleException(it))
        }
    }
}
