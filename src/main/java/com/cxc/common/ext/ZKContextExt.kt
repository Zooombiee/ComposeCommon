package com.cxc.common.ext

import android.content.Context
import androidx.fragment.app.Fragment

val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

val Context.statusBarHeight
    get() = resources.getDimensionPixelSize(
        resources.getIdentifier(
            "status_bar_height",
            "dimen",
            "android"
        )
    )

fun Fragment.getColor(color: Int) = requireContext().getColor(color)


