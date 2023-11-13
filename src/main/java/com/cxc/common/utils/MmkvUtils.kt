package com.cxc.common.utils

import android.os.Parcelable
import android.text.TextUtils
import com.blankj.utilcode.util.AppUtils
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type


object MmkvUtils {

    var mmkv = MMKV.defaultMMKV(1, "${AppUtils.getAppName()}:${AppUtils.getAppPackageName()}")

    /**
     * 存储常规数据
     */
    fun put(key: String, content: Any?): Boolean {
        if (TextUtils.isEmpty(key) || content == null) return false
        try {
            when (content) {
                is String -> {
                    mmkv.encode(key, content)
                }

                is Double -> {
                    mmkv.encode(key, content)
                }

                is Float -> {
                    mmkv.encode(key, content)
                }

                is Int -> {
                    mmkv.encode(key, content)
                }

                is Parcelable -> {
                    mmkv.encode(key, content)
                }

                is ByteArray -> {
                    mmkv.encode(key, content)
                }

                is Long -> {
                    mmkv.encode(key, content)
                }

                is Boolean -> {
                    mmkv.encode(key, content)
                }

                else -> {
                    return false
                }
            }
        } catch (exception: Exception) {
            return false
        }
        return true
    }

    /**
     * 存储set集合
     */
    fun putSet(key: String, content: Set<String>): Boolean {
        if (TextUtils.isEmpty(key) || content == null) return false

        mmkv.encode(key, content!!)
        return true
    }


    /**
     * 获取String 数据
     */
    fun getString(key: String): String {

        return mmkv.decodeString(key) ?: ""
    }

    fun getString(key: String, defult: String): String {

        if (TextUtils.isEmpty(mmkv.decodeString(key))) return ""
        return mmkv.decodeString(key) ?: ""
    }

    fun getDouble(key: String): Double {

        return mmkv.decodeDouble(key)
    }

    fun getDouble(key: String, defult: Double): Double {

        if (mmkv.decodeDouble(key) == 0.0) return defult
        return mmkv.decodeDouble(key)
    }

    fun getFloat(key: String): Float {

        return mmkv.decodeFloat(key)
    }

    fun getFloat(key: String, defult: Float): Float {

        if (mmkv.decodeDouble(key) == 0.0) return defult
        return mmkv.decodeFloat(key)
    }

    fun getInt(key: String): Int {

        return mmkv.decodeInt(key)
    }

    fun getInt(key: String, defult: Int): Int {

        if (mmkv.decodeInt(key) == 0) return defult
        return mmkv.decodeInt(key)
    }

    fun getLong(key: String, defult: Int): Long {

        if (mmkv.decodeInt(key) == 0) return defult.toLong()
        return mmkv.decodeLong(key)
    }

    fun getBoolean(key: String, defult: Boolean = false): Boolean {

        return mmkv?.decodeBool(key) ?: defult
    }

    fun getByteArray(key: String): ByteArray? {

        return mmkv.decodeBytes(key) ?: null
    }

    fun getByteArray(key: String, defult: Int): ByteArray? {

        return mmkv.decodeBytes(key) ?: null
    }

    /**
     * 获取存储的对象  对象必须实现Parcelable
     */
    fun <T : Parcelable?> getParcelable(key: String?, tClass: Class<T>?): T? {

        return mmkv?.decodeParcelable(key, tClass)
    }

    /**
     * 获取存储的set<String>集合
     */
    fun getSet(key: String): Set<String>? {

        return mmkv.decodeStringSet(key) ?: null
    }

    /**
     * 存储集合 对象必须实现Parcelable
     */
    fun putList(key: String, data: MutableList<out Any>) {
        put(key, Gson().toJson(data))
    }

    /**
     * 字符串解析成集合
     */
    fun <T> getList(key: String, type: Type?): ArrayList<T>? {
        val list: ArrayList<T>? = Gson().fromJson(getString(key), type)
        return list
    }

    /*** 移除某个key对 ** @param key  */
    fun removeKey(key: String?) {
        mmkv.removeValueForKey(key)
    }


}