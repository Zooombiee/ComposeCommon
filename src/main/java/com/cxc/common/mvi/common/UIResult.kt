package com.cxc.common.mvi.common

import com.cxc.common.http.httpbase.exception.AppException
import com.cxc.common.http.httpbase.exception.ExceptionHandle
import com.cxc.common.mvi.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 *@Date: 2022/11/21
 *@Time: 11:39
 *@Author:cxc
 *@Description:定义公用的UIState,可以用在有缺省页的页面
 */
sealed interface UIResult<out T> : UiState {
    object Loading : UIResult<Nothing>
    object Empty : UIResult<Nothing>
    data class Error(val exception: AppException) : UIResult<Nothing>
    data class Success<T>(val data: T) : UIResult<T>
}

fun <T> Flow<T>.asResult(): Flow<UIResult<T>> {
    return this
        .map<T, UIResult<T>> {
            UIResult.Success(it)
        }
        .onStart { emit(UIResult.Loading) }
        .catch { emit(UIResult.Error(ExceptionHandle.handleException(it))) }
}