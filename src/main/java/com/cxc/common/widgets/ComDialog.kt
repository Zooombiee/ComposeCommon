package com.cxc.common.widgets

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.cxc.common.ext.stringRes
import com.cxc.compose_common.R
import kotlinx.coroutines.launch

val moeDialogQueue = mutableStateListOf<ComDialogData>()

data class ComDialogData(
    val text: String,
    val title: String? = null,
    val modifier: Modifier = Modifier,
    val onDismiss: ((ComDialogData) -> Unit)? = null,
    val onConfirm: ((ComDialogData) -> Unit)? = null,
    val dismissLabel: String = stringRes(R.string.common_cancel),
    val confirmLabel: String = stringRes(R.string.common_confirm),
    val properties: DialogProperties = DialogProperties(),
    val content: @Composable ((ComDialogData) -> Unit)? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComDialog() {
    moeDialogQueue.forEach {
        if (it.content == null) {
            AlertDialog(
                modifier = it.modifier,
                onDismissRequest = { it.dismiss() },
                properties = it.properties,
                title = {it.title?.let { Text(it) }},
                text = { Text(text = it.text) },
                confirmButton = {
                    if (it.onConfirm != null) {
                        TextButton(onClick = {
                            it.onConfirm.invoke(it)
                            it.dismiss()
                        }) {
                            Text(text = it.confirmLabel)
                        }
                    }
                },
                dismissButton = {
                    it.onDismiss?.run {
                    TextButton(onClick = {
                        it.onDismiss.invoke(it)
                        it.dismiss()
                    }) {
                        Text(text = it.dismissLabel)
                    }
                }}
            )
        } else {
            AlertDialog(
                onDismissRequest = { it.dismiss() },
                properties = it.properties,
                modifier = it.modifier,
                content = { it.content.invoke(it) }
            )
        }
    }
}

fun ComDialogData.dismiss() = apply {
    mainMoeScope.launch {
        moeDialogQueue.remove(this@dismiss)
    }
}

fun ComDialogData.show() = apply {
    mainMoeScope.launch {
        moeDialogQueue.add(this@show)
    }
}

fun String.comDialog(
    title: String? = null,
    onConfirm: ((ComDialogData) -> Unit)? = null,
    onDismiss: ((ComDialogData) -> Unit)? = null,
    confirmLabel: String = stringRes(R.string.common_confirm),
    dismissLabel: String = stringRes(R.string.common_cancel),
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    content: @Composable ((ComDialogData) -> Unit)? = null,
) = ComDialogData(
    this,
    title,
    modifier,
    onDismiss,
    onConfirm,
    dismissLabel,
    confirmLabel,
    properties,
    content = content
).apply { show() }

fun Any?.dialog() = this.toString().comDialog()