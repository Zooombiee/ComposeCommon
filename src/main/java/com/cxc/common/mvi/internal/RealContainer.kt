package com.cxc.common.mvi.internal

import com.cxc.common.mvi.MutableContainer
import com.cxc.common.mvi.UiEvent
import com.cxc.common.mvi.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class RealContainer<S : UiState, E : UiEvent>(
    initialState: S,
    private val parentScope: CoroutineScope,
) : MutableContainer<S, E> {

    private val _internalStateFlow = MutableStateFlow(initialState)

    private val _internalSingleEventSharedFlow = MutableSharedFlow<E>()

    override val uiStateFlow: StateFlow<S> = _internalStateFlow

    override val singleEventFlow: Flow<E> = _internalSingleEventSharedFlow

    override fun updateState(action: S.() -> S) {
        _internalStateFlow.update { action(_internalStateFlow.value) }
    }

    override fun sendEvent(event: E) {
        parentScope.launch {
            _internalSingleEventSharedFlow.emit(event)
        }
    }

}