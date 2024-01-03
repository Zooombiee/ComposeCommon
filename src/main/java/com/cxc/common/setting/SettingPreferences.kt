package com.cxc.common.setting

import android.app.Application
import com.cxc.common.preference.preferences.PreferenceStore
import com.cxc.common.preference.preferences.getEnum
import com.cxc.common.theme.EasyThemeMode

/**
 * 设置
 * Created by HeYanLe on 2023/7/29 17:39.
 * https://github.com/heyanLE
 */
class SettingPreferences  constructor(
    private val application: Application,
    private val preferenceStore: PreferenceStore
) {

    // 无痕模式
    val isInPrivate = preferenceStore.getBoolean("in_private", false)

    // 外观设置
    // 夜间模式
    enum class DarkMode {
        Auto, Dark, Light
    }
    val darkMode = preferenceStore.getEnum<DarkMode>("dark_mode", DarkMode.Auto)

    // 主题设置
    val isThemeDynamic = preferenceStore.getBoolean("theme_dynamic", true)
    val themeMode = preferenceStore.getEnum<EasyThemeMode>("theme_mode", EasyThemeMode.Default)


}