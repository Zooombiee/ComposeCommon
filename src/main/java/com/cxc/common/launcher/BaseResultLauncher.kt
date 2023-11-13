package com.cxc.common.launcher

import android.annotation.SuppressLint
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.MutableLiveData

/**
 * @Date: 2022/9/14
 * @Time: 14:32
 * @Author:cxc
 * @Description: 对Result-Api的封装，支持各种输入与输出，使用泛型定义
 */
open class BaseResultLauncher<I, O>(
    caller: ActivityResultCaller,
    contract: ActivityResultContract<I, O>
) {
    private val launcher: ActivityResultLauncher<I>
    private var callback: ActivityResultCallback<O>? = null
    private val unprocessedResult: MutableLiveData<O>? = null
    fun launch(@SuppressLint("UnknownNullness") input: I, callback: ActivityResultCallback<O>) {
        launch(input, null, callback)
    }

    fun launch(
        @SuppressLint("UnknownNullness") input: I,
        options: ActivityOptionsCompat?,
        callback: ActivityResultCallback<O>
    ) {
        this.callback = callback
        launcher.launch(input, options)
    }

    init {
        launcher = caller.registerForActivityResult(contract) { result: O ->
            if (callback != null) {
                callback!!.onActivityResult(result)
                callback = null
            }
        }
    }
}