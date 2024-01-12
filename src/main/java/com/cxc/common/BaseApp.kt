package com.cxc.common

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.tencent.mmkv.MMKV


lateinit var APP: BaseApp

open class BaseApp : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        APP = this
        if (isMainProcess()) {
            MMKV.initialize(this)
        }


    }

    private fun isMainProcess(): Boolean {
        return packageName == if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getProcessName()
        } else {
            getProcessName(this) ?: packageName
        }

    }

    private fun getProcessName(cxt: Context): String? {
        val pid = Process.myPid()
        val am = cxt.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return null
        for (procInfo in runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName
            }
        }
        return null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}