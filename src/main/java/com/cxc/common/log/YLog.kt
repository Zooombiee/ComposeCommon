package com.cxc.common.log

import android.util.Log
import com.cxc.compose_common.BuildConfig

/**
 *@Date: 2023/7/25
 *@Time: 17:35
 *@Author:cxc
 *@Description:
 */
object YLog {

    var enabled = BuildConfig.DEBUG

    private enum class LEVEL {
        V, D, I, W, E
    }

    fun d(tag: String,message:String){
        log(LEVEL.D,tag,message)
    }
    fun i(tag: String,message:String){
        log(LEVEL.I,tag,message)
    }
    fun v(tag: String,message:String){
        log(LEVEL.V,tag,message)
    }
    fun w(tag: String,message:String){
        log(LEVEL.W,tag,message)
    }
    fun e(tag: String,e:String){
        log(LEVEL.E,tag,e)
    }


    private fun log(level: LEVEL, tag: String, message: String) {
        if (!enabled) return
        when (level) {
            LEVEL.V -> Log.v(tag, message)
            LEVEL.D -> Log.d(tag, message)
            LEVEL.I -> Log.i(tag, message)
            LEVEL.W -> Log.w(tag, message)
            LEVEL.E -> Log.e(tag, message)
        }
    }
}