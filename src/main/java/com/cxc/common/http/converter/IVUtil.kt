package com.cxc.common.http.converter

/**
 *@Date: 2023/7/11
 *@Time: 19:03
 *@Author:cxc
 *@Description: 创建请求IV工具类
 */
object IVUtil {

    private val ivHashMap = HashMap<String, IVParameter>()

    /**
     * 获取随机iv字符串 并缓存
     */
    fun getRandomIVParameter(): IVParameter = getRandomString(16).toIVParameter().apply {
        saveToIvMap(this)
    }


    fun String.toIVParameter(): IVParameter {
        val split1 = 4
        val split2 = this.length - 4
        val iv1 = this.substring(0, split1)
        val ivCenter = this.substring(split1, split2)
        val iv2 = this.substring(split2)

        return IVParameter(iv1, ivCenter, iv2)
    }

    fun saveToIvMap(ivParameter: IVParameter) {
        ivHashMap[ivParameter.getKey()] = ivParameter
    }

    fun getIVFromCache(key: String): IVParameter? {
        var iv: IVParameter? = null
        if (ivHashMap.containsKey(key)) {
            iv = ivHashMap[key]
        }
        return iv
    }

    fun removeFormCache(key: String) {
        if (ivHashMap.containsKey(key)) {
            ivHashMap.remove(key)
        }
    }

    fun getRandomString(length: Int): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXTZ"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}

class IVParameter(val first: String, val second: String, val third: String) {
    fun toFullString(): String = first + second + third

    fun getKey() = first+third
}