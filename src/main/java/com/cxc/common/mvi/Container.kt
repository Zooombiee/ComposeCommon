package com.cxc.common.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * 状态容器，分别存储UI状态和单次事件，如果不包含单次事件，则使用[Nothing]
 */
interface Container<S : UiState, E : UiEvent> {

    //ui状态流
    val uiStateFlow: StateFlow<S>

    //单次事件流
    val singleEventFlow: Flow<E>

}