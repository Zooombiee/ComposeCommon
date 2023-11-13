package com.cxc.common.http.httpbase.impl;


import com.cxc.common.http.httpbase.ext.RetrofitUrlManager;

import okhttp3.HttpUrl;

/**
 * ================================================
 * Url解析器
 * <p>
 * Created by JessYan on 17/07/2017 17:44
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */

public interface UrlParser {

    /**
     * 这里可以做一些初始化操作
     *
     * @param retrofitUrlManager {@link RetrofitUrlManager}
     */
    void init(RetrofitUrlManager retrofitUrlManager);

    /**
     * 将 中映射的 URL 解析成完整的{@link HttpUrl}
     * 用来替换 达到动态切换 URL
     *
     * @param domainUrl 用于替换的 URL 地址
     * @param url       旧 URL 地址
     * @return
     */
    HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url);
}