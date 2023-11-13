package com.cxc.common.ext

import android.app.Activity
import android.view.Window
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

/**
 *@Date: 2022/9/19
 *@Time: 17:44
 *@Author:cxc
 *@Description:动画效果拓展 后续有需要再更新
 */

//进入
fun Activity.enterContainerTransform() {
    window.sharedElementEnterTransition=  buildContainerTransform(true)
}

//离开
fun Activity.returnContainerTransform() {
    window.sharedElementReturnTransition = buildContainerTransform(false)
}

/**
 * 通用的变换动画
 * @receiver Activity
 * @param context Context
 * @param enter Boolean  true 进入 false 离开
 * @return MaterialContainerTransform
 */

fun Activity.buildContainerTransform( enter: Boolean): MaterialContainerTransform {

    return MaterialContainerTransform(this, enter).apply {
//        setAllContainerColors(
//            MaterialColors.getColor(findViewById(android.R.id.content), R.attr.colorSurface)
//        )
        addTarget(android.R.id.content)
    }

}

fun  Activity.addEnterCallback(){
    window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
}

fun  Activity.addExitCallback(){
    window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
    window.sharedElementsUseOverlay = false
}