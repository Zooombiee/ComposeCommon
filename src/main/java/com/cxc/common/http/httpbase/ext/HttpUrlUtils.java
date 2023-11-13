package com.cxc.common.http.httpbase.ext;


import com.cxc.common.http.httpbase.exception.InvalidUrlException;

import okhttp3.HttpUrl;

/**
 * Url检查工具类
 */
public class HttpUrlUtils {

    private HttpUrlUtils() {
        throw new IllegalStateException("do not instantiation me");
    }

    static HttpUrl checkUrl(String url) {
        HttpUrl parseUrl = HttpUrl.parse(url);
        if (null == parseUrl) {
            throw new InvalidUrlException(url);
        } else {
            return parseUrl;
        }
    }

    static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }
}
