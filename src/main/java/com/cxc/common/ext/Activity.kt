package com.cxc.common.ext

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 *@Date: 2022/9/6
 *@Time: 16:00
 *@Author:cxc
 *@Description:
 */

/**
 * startActivity拓展
 * @receiver Activity
 * @param block 传递的intent内容
 */
inline fun <reified T> Activity.startActivity( options: Bundle? = null,block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block),options)
}

inline fun <reified T> Activity.startActivity(vararg  pair: Pair<String,Any>) {
    startActivity(Intent(this, T::class.java).apply {
        putExtras(bundleOf(*pair))
    } )
}

inline fun <reified T> Fragment.gotoActivity(
    options: Bundle? = null,
    block: Intent.() -> Unit = {}
) {
    activity?.startActivity<T>(options,block)
}

inline fun <reified T> Fragment.gotoActivity(
    vararg  pair: Pair<String,Any>
) {
    activity?.startActivity<T>(*pair)
}

//带参数finis
inline fun <reified T> Activity.finishWithResult(vararg  pair: Pair<String,Any>) {
    val intent = Intent().apply {  putExtras(bundleOf(*pair)) }
    setResult(RESULT_OK, intent)
    finish()
}




//ViewModel
fun <T : ViewModel> Fragment.getVM(clazz: Class<T>) = ViewModelProvider(this)[clazz]

fun <T : ViewModel> Fragment.getActivityVM(clazz: Class<T>) = ViewModelProvider(requireActivity())[clazz]

fun <T : ViewModel> AppCompatActivity.getVM(clazz: Class<T>) = ViewModelProvider(this)[clazz]