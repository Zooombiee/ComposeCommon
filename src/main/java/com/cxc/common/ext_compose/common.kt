package com.cxc.common.ext_compose

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope

/**
 *@Date: 2023/11/14
 *@Time: 10:54
 *@Author:cxc
 *@Description:
 */

@Composable
fun SpacerW(value: Int) {
    return Spacer(modifier = Modifier.width(value.dp))
}

@Composable
fun SpacerH(value: Int) {
    return Spacer(modifier = Modifier.height(value.dp))
}

@Composable
fun LaunchUnit(block: suspend CoroutineScope.() -> Unit) {
    LaunchedEffect(Unit) {
        block()
    }
}

@Composable
fun GotoWeb(openUrlContract: ManagedActivityResultLauncher<Intent, ActivityResult>, url: String) {
    SideEffect {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        openUrlContract.launch(intent)
    }
}