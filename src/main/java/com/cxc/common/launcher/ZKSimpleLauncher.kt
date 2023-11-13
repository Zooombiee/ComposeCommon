package com.cxc.common.launcher

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.core.os.bundleOf

/**
 *@Date: 2022/9/14
 *@Time: 14:38
 *@Author:cxc
 *@Description:
 */
/**
 * 一般我们用这一个-StartActivityForResult 的 Launcher
 */
class ZKSimpleLauncher(private val caller: ActivityResultCaller) :
    BaseResultLauncher<Intent, ActivityResult>(
        caller,
        ActivityResultContracts.StartActivityForResult()
    ) {

    //封装Intent的启动方式
    inline fun <reified T> launch(
        bundle: Array<out Pair<String, Any?>>? = null,
        @NonNull callback: ActivityResultCallback<ActivityResult>
    ) {

        commContext()?.apply {
            val intent = Intent(commContext(), T::class.java).apply {
                if (bundle != null) {
                    putExtras(bundleOf(*bundle))
                }
            }

            launch(intent, null, callback)
        }

    }


    /**
     * 获取context
     * @return Context?
     */
    fun commContext(): Context? {
        return if (caller is Context) caller else null
    }


}
