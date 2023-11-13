package com.cxc.compose_common.ext

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cxc.common.application.ZKBaseApplication
import com.cxc.common.utils.ZKDpUtil

/**
 * Created by Fushize on 2021/7/23.
 */


/**
 * 快速转px
 */
val Float.px: Float
    get() = if (this == 0f) 0f else ZKDpUtil.px2dp(this).toFloat()

val Int.px: Int
    get() = if (this == 0) 0 else ZKDpUtil.px2dp(this)


/**
 * 快速转dp的扩展函数
 */
val Float.dp: Float
    get() = if (this == 0f) 0f else ZKDpUtil.dp2px(this).toFloat()

val Int.dp: Int
    get() = if (this == 0) 0 else ZKDpUtil.dp2px(this)

/**
 * 获取颜色
 */
fun Int.asColor() = ContextCompat.getColor(ZKBaseApplication.getInstance(), this)

/**
 * 获取drawable
 */
fun Int.asDrawable() = ContextCompat.getDrawable(ZKBaseApplication.getInstance(), this)


fun View.isVisible() = this.visibility == View.VISIBLE
fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

// //*****************点击防抖**********************//
// private var <T : View>T.triggerLastTime: Long
//     get() = if (getTag(R.id.triggerLastTimeKey) != null) getTag(R.id.triggerLastTimeKey) as Long else 0
//     set(value) {
//         setTag(R.id.triggerLastTimeKey, value)
//     }
//
// /**
//  * 给view添加一个延迟的属性（用来屏蔽连击操作）
//  */
// private var <T : View> T.triggerDelay: Long
//     get() = if (getTag(R.id.triggerDelayKey) != null) getTag(R.id.triggerDelayKey) as Long else -1
//     set(value) {
//         setTag(R.id.triggerDelayKey, value)
//     }
//
// fun View.clickAble(): Boolean {
//     var clickable = false
//     val currentClickTime = System.currentTimeMillis()
//     if (currentClickTime - triggerLastTime >= triggerDelay) {
//         triggerLastTime = currentClickTime
//         clickable = true
//     }
//
//     return clickable
// }
//
// //设置一个防抖的点击事件
// fun <T : View> T.singleClick(delay: Long = 1000L, block: (T) -> Unit) {
//     triggerDelay = delay
//     setOnClickListener {
//         if (clickAble()) {
//             block(this)
//         }
//     }
// }
// //*****************点击防抖**********************//
//
// fun <T, VH : BaseViewHolder> BaseQuickAdapter<T, VH>.getDrawable(@DrawableRes id: Int): Drawable {
//     return AppCompatResources.getDrawable(context, id) ?: ColorDrawable(Color.TRANSPARENT)
// }
//
// fun Fragment.getDrawable(@DrawableRes id: Int): Drawable {
//     return AppCompatResources.getDrawable(requireContext(), id) ?: ColorDrawable(Color.TRANSPARENT)
// }


