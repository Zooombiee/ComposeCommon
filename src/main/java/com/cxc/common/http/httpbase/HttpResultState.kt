package com.cxc.common.http.httpbase

import androidx.lifecycle.MutableLiveData
import com.cxc.common.http.httpbase.exception.AppException

/**
 * 作者　: hegaojian
 * 时间　: 2020/4/9
 * 描述　: 自定义结果集封装类
 */
sealed class HttpResultState<out T> {

    companion object {

        fun <T> onHttpSuccess(data: T): HttpResultState<T> = Success(data)

        fun <T> onAppLoading(loadingMessage: String): HttpResultState<T> = Loading(loadingMessage)

        fun <T> onHttpError(error: AppException): HttpResultState<T> = Error(error)

        fun <T> onHttpStart(startMessage: String): HttpResultState<T> = Start(startMessage)
    }

    data class Loading(val loadingMessage: String) : HttpResultState<Nothing>()
    data class Success<out T>(val data: T) : HttpResultState<T>()
    data class Error(val error: AppException) : HttpResultState<Nothing>()
    //请求开始，有些时候不想要加载框，只想获得开始状态
    data class Start(val startMessage: String) : HttpResultState<Nothing>()
}

// /**
//  * 处理返回值
//  * @param result 请求结果
//  */
// fun <T> UnPeekLiveData<HttpResultState<T>>.paresResult(result: BaseResponse<T>) {
//     value = when {
//         result.isSuccess() -> {
//             HttpResultState.onHttpSuccess(result.getResponseData())
//         }
//         else -> {
//             HttpResultState.onHttpError(
//                 AppException(
//                     result.getResponseCode(),
//                     result.getResponseMsg()
//                 )
//             )
//         }
//     }
// }

/**
 * 不处理返回值 直接返回请求结果
 * @param result 请求结果
 */
fun <T> MutableLiveData<HttpResultState<T>>.paresResult(result: T) {
    value = HttpResultState.onHttpSuccess(result)
}

// /**
//  * 异常转换异常处理
//  */
// fun <T> UnPeekLiveData<HttpResultState<T>>.paresException(e: Throwable) {
//     this.value = HttpResultState.onHttpError(ExceptionHandle.handleException(e))
// }
//
//
// /**
//  * 不处理返回值 直接返回请求结果
//  * @param result 请求结果
//  */
// suspend fun <T> FlowCollector<HttpResultState<T>>.paresResult(result: BaseResponse<T>): Boolean {
//    emit(when {
//        result.isSuccess() -> {
//            HttpResultState.onHttpSuccess(result.getResponseData())
//        }
//        else -> {
//            HttpResultState.onHttpError(
//                AppException(
//                    result.getResponseCode(),
//                    result.getResponseMsg()
//                )
//            )
//        }
//    })
//     return result.isSuccess()
// }

