package com.cxc.common.http.httpbase.exception;

import android.text.TextUtils;

/**
 * ================================================
 * Url 无效的异常
 * <p>
 * Created by JessYan on 2017/7/24.
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class InvalidUrlException extends RuntimeException {

    public InvalidUrlException(String url) {
        super("You've configured an invalid url : " + (TextUtils.isEmpty(url) ? "EMPTY_OR_NULL_URL" : url));
    }
}