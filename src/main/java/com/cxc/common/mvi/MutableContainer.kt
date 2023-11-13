package com.cxc.common.mvi

interface MutableContainer<S : UiState, E : UiEvent> :
    Container<S, E> {

    fun updateState(action: S.() -> S)

    fun sendEvent(event: E)

}