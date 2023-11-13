package com.cxc.common.http.converter

import com.cxc.common.AppConfig
import com.cxc.common.http.HttpsUtils
import com.cxc.common.http.httpbase.PersistentCookieJar
import com.cxc.common.http.httpbase.SharedPrefsCookiePersistor
import com.cxc.common.http.httpbase.appContext
import com.cxc.common.http.httpbase.cache.SetCookieCache
import com.cxc.common.http.httpbase.ext.RetrofitUrlManager
import com.cxc.common.http.httpbase.interceptor.CacheInterceptor
import com.cxc.common.http.interceptor.ZKParamsInterceptor
import com.google.gson.GsonBuilder
import com.cxc.common.http.interceptor.YHAppHeaderInterceptor
import com.cxc.common.http.interceptor.YHConverterFactory
import com.cxc.common.http.interceptor.ZKHttpErrorInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

fun <T : Any> getApi(serviceClass: Class<T>) = YHHttpApi.INSTANCE.getApi(serviceClass)
class YHHttpApi {

    private val maxSizeCache: Long = 10 * 1024 * 1024               //最大缓存

    //缓存请求API对象
    private val apiCacheMap = ConcurrentHashMap<String, Any>()

    companion object {

        private val BASE_URL: String = AppConfig.BASE_URL
        val INSTANCE: YHHttpApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            YHHttpApi()
        }
    }

    /**
     * 配置http
     */
    private val okHttpClient: OkHttpClient
        get() {
            var builder = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
            builder = setHttpClientBuilder(builder)

            //给client的builder添加了一个socketFactory
            builder.sslSocketFactory(
                HttpsUtils.getSslSocketFactory().sSLSocketFactory,
                HttpsUtils.getSslSocketFactory().trustManager
            )
            return builder.build()
        }

    private val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(appContext)
        )
    }

    /**
     * 网络请求的Api调用方法，不需要传入根路径
     */
    fun <T> getApi(serviceClass: Class<T>): T {
        val cacheApi = apiCacheMap[serviceClass.name]
        if (cacheApi != null) {
            return cacheApi as T
        }
        synchronized(apiCacheMap) {
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
            val apiClass = setRetrofitBuilder(retrofitBuilder).build().create(serviceClass)
            apiCacheMap[serviceClass.name] = apiClass!!
            return apiClass
        }
    }

    /**
     * 网络请求Api调用方法，传入根路径
     */
    fun <T> getApi(serviceClass: Class<T>, baseUrl: String): T {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
        return setRetrofitBuilder(retrofitBuilder).build().create(serviceClass)
    }

    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    private fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            //设置缓存配置 缓存最大10M
            cache(Cache(File(appContext.cacheDir, "cxk_cache"), maxSizeCache))
            //添加Cookies自动持久化
            cookieJar(cookieJar)
            //添加共用参数
            addInterceptor(ZKParamsInterceptor())
            /**
             * 1.添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
             * 2.添加在共用参数之后，因为header里的sign依赖共用参数
             */

            addInterceptor(YHAppHeaderInterceptor())
            //            addInterceptor(MyAppHeaderInterceptor())
            //添加缓存拦截器 可传入缓存天数，不传默认7天
            addInterceptor(CacheInterceptor())
            // 日志拦截器
            // if (!GlobalConstant.isRelease) {
            //     addInterceptor(LogInterceptor())
            // }
            //添加返回数据拦截器
            addInterceptor(ZKHttpErrorInterceptor())
            // if (!GlobalConstant.isRelease) { //请将Pandora作为最后一个拦截器，以防request-headers, request-params获取不到
                //添加pandora
                // addInterceptor(Pandora.get().interceptor)
            // }
            //超时时间 连接、读、写
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
        }
        return builder
    }

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，protobuf等
     */
    private fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            // addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            addConverterFactory(YHConverterFactory.create(GsonBuilder().create()))
        }
    }
}