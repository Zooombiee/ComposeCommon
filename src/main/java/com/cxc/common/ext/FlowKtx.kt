package com.cxc.common.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cxc.common.http.httpbase.HttpResultState
import com.cxc.common.http.httpbase.exception.ExceptionHandle
import com.cxc.common.mvi.UiEvent
import com.cxc.common.mvi.UiState
import com.cxc.common.mvi.internal.StateTuple2
import com.cxc.common.mvi.internal.StateTuple3
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

/**
 * flow的包裹
 * @receiver Flow<T>
 * @param lifecycleOwner LifecycleOwner
 * @param state State
 * @param option 是否需要额外的操作
 * @param action Function1<T, Unit>
 */
fun <T> Flow<T>.collectWrapper(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    option: Flow<T>.() -> Flow<T> = { this },
    action: (T) -> Unit
) = lifecycleOwner.lifecycleScope.launch {
    this@collectWrapper
        .flowWithLifecycle(lifecycleOwner.lifecycle, state)
        .option()
        .collectLatest {
            action(it)
        }
}


/**
 * 异常转换异常处理
 */
suspend fun <T> FlowCollector<HttpResultState<T>>.paresException(e: Throwable) {
    emit(HttpResultState.onHttpError(ExceptionHandle.handleException(e)))
}

// suspend fun <T> FlowCollector<T>.requestCatching(
//     onSuccess:((T)->Unit)? = null,
//     block:suspend ()->ZKResponse<T>,
// ){
//     runCatching {
//         //请求体
//        block()
//     }.onSuccess {
//         when {
//             it.isSuccess() -> {
//                 emit(it.getResponseData())
//                 onSuccess?.invoke(it.getResponseData())
//             }
//             else -> {
//                 throw AppException(
//                     it.getResponseCode(),
//                     it.getResponseMsg(),
//                     it.getResponseMsg()
//                 )
//             }
//         }
//
//     }.onFailure { //网络层错误
//         //失败回调
//         throw  ExceptionHandle.handleException(it)
//     }
// }


fun <T : UiState> Flow<T>.collectState(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    action: StateCollector<T>.() -> Unit
) {
    StateCollector(this@collectState, lifecycleOwner, state).action()
}

fun <T : UiEvent> Flow<T>.collectSingleEvent(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    action: (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(state) {
            this@collectSingleEvent.collect {
                action(it)
            }
        }
    }
}

class StateCollector<T : UiState>(
    private val flow: Flow<T>,
    private val lifecycleOwner: LifecycleOwner,
    private val state: Lifecycle.State
) {

    fun <A> collectPartial(
        prop1: KProperty1<T, A>,
        action: (A) -> Unit
    ) {
        flow.collectPartial(lifecycleOwner, prop1, state, action)
    }

    fun <A, B> collectPartial(
        prop1: KProperty1<T, A>,
        prop2: KProperty1<T, B>,
        action: (A, B) -> Unit
    ) {
        flow.collectPartial(lifecycleOwner, prop1, prop2, state, action)
    }

    fun <A, B, C> collectPartial(
        prop1: KProperty1<T, A>,
        prop2: KProperty1<T, B>,
        prop3: KProperty1<T, C>,
        action: (A, B, C) -> Unit
    ) {
        flow.collectPartial(lifecycleOwner, prop1, prop2, prop3, state, action)
    }
}

internal fun <T : UiState, A> Flow<T>.collectPartial(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    state: Lifecycle.State,
    action: (A) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(state) {
            this@collectPartial
                .map { prop1.get(it) }
                .distinctUntilChanged()
                .collect { partialState ->
                    action(partialState)
                }
        }
    }
}

internal fun <T : UiState, A, B> Flow<T>.collectPartial(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    action: (A, B) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(state) {
            this@collectPartial
                .map { StateTuple2(prop1.get(it), prop2.get(it)) }
                .distinctUntilChanged()
                .collect { (partialStateA, partialStateB) ->
                    action(partialStateA, partialStateB)
                }
        }
    }
}

internal fun <T : UiState, A, B, C> Flow<T>.collectPartial(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    prop3: KProperty1<T, C>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    action: (A, B, C) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(state) {
            this@collectPartial
                .map { StateTuple3(prop1.get(it), prop2.get(it), prop3.get(it)) }
                .distinctUntilChanged()
                .collect { (partialStateA, partialStateB, partialStateC) ->
                    action(partialStateA, partialStateB, partialStateC)
                }
        }
    }
}



