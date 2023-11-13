package com.cxc.common.ext

import com.cxc.common.http.httpbase.exception.AppException

/**
 *@Date: 2023/11/9
 *@Time: 20:58
 *@Author:cxc
 *@Description:
 */

class HttpResultCallBack<T> {
    //成功
    var success: ((T) -> Unit)? = null

    //失败
    var failed: ((AppException) -> Unit)? = null

    //加载
    var loading: (() -> Unit)? = null

    //请求开始
    var start: (() -> Unit)? = null

    fun onSuccess(call: (T) -> Unit) {
        success = call
    }

    fun onFailed(call: (AppException) -> Unit) {
        failed = call
    }

    fun onStart(call: () -> Unit) {
        start = call
    }

    fun onLoading(call: () -> Unit) {
        loading = call
    }
}