package com.cxc.common.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.cxc.common.ext.loge
import com.cxc.common.setting.SettingPreferences
import org.koin.compose.koinInject

/**
 * Created by HeYanLe on 2023/2/19 0:02.
 * https://github.com/heyanLE
 */

@Composable
fun NormalSystemBarColor(
    getStatusBarDark: (Boolean)->Boolean = {!it},
    getNavigationBarDark: (Boolean)->Boolean = {!it},
){
    val themeController: EasyThemeController = koinInject()
    val themeState by themeController.themeFlow.collectAsState()
    val isDark = when (themeState.darkMode) {
        SettingPreferences.DarkMode.Dark -> true
        SettingPreferences.DarkMode.Light -> false
        else -> isSystemInDarkTheme()
    }

    // val uiController = rememberSystemUiController()
    // val view = LocalView.current
    // if (!view.isInEditMode) {
    //     SideEffect() {
    //         uiController.setStatusBarColor(Color.Transparent, getStatusBarDark(isDark))
    //         uiController.setNavigationBarColor(Color.Transparent, getNavigationBarDark(isDark))
    //     }
    // }
}

@Composable
fun EasyTheme(
    themeController: EasyThemeController = koinInject(),
    content: @Composable () -> Unit,
) {

    val themeState by themeController.themeFlow.collectAsState()
    themeState.loge("EasyTheme")

    val isDynamic = themeState.isDynamicColor && themeController.isSupportDynamicColor()
    val isDark = when (themeState.darkMode) {
        SettingPreferences.DarkMode.Dark -> true
        SettingPreferences.DarkMode.Light -> false
        else -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        isDynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> {
            themeState.themeMode.getColorScheme(isDark)
        }
    }

    MaterialTheme(
        colorScheme =  colorScheme,
        typography = Typography,
        content = content
    )
}