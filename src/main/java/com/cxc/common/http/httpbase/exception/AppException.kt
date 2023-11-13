package com.cxc.common.http.httpbase.exception

import com.cxc.common.http.httpbase.Error

/**
 * App异常自定义出来对象
 */
class AppException : Exception {

    var errorMsg: String                    //错误消息
    var errCode: Int = 0                    //错误码
    var errorLog: String?                   //错误日志
    var throwable: Throwable? = null        //异常信息

    constructor(errCode: Int, error: String?, errorLog: String? = "", throwable: Throwable? = null) : super(error) {
        this.errorMsg = error ?: "请求失败，请稍后再试"
        this.errCode = errCode
        this.errorLog = errorLog ?: this.errorMsg
        this.throwable = throwable
    }

    constructor(error: Error, e: Throwable?) {
        errCode = error.getKey()
        errorMsg = error.getValue()
        errorLog = e?.message
        throwable = e
    }
    constructor(error:Error,msg:String){
        errCode = error.getKey()
        errorMsg = msg
        errorLog =msg
    }

}