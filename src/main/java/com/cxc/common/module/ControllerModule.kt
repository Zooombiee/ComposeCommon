package com.cxc.common.module

import com.cxc.common.APP
import com.cxc.common.preference.preferences.android.AndroidPreference
import com.cxc.common.preference.preferences.android.AndroidPreferenceStore
import com.cxc.common.setting.SettingPreferences
import com.cxc.common.theme.EasyThemeController
import org.koin.dsl.module

/**
 *@Date: 2023/11/13
 *@Time: 17:17
 *@Author:cxc
 *@Description:
 */

val controllerModule = module {

    single { AndroidPreferenceStore(APP) }

    single { SettingPreferences(APP,get<AndroidPreferenceStore>()) }

    single { EasyThemeController(get()) }

}