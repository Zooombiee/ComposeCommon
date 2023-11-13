package com.cxc.common.ext

import com.cxc.common.http.BaseResponse
import com.cxc.common.http.httpbase.exception.AppException

/**
 *@Date: 2022/9/15
 *@Time: 14:40
 *@Author:cxc
 *@Description:包装一个flow版本的请求回调
 */



/**
 * 处理返回值
 * @param result 请求结果
 */
fun <T> paresResult(result: BaseResponse<T>): T {
    return when {
        result.isSuccess() -> {
            result.getResponseData()
        }
        //直接抛异常
        else -> throw   AppException(
            result.getResponseCode(),
            result.getResponseMsg()
        )
    }

}
