package com.cxc.common.mvi

import com.cxc.common.mvi.internal.RealContainer
import kotlinx.coroutines.CoroutineScope

class ContainerLazy<S : UiState, E : UiEvent>(
    initialState: S,
    parentScope: CoroutineScope
) : Lazy<MutableContainer<S, E>> {

    private var cached: MutableContainer<S, E>? = null

    override val value: MutableContainer<S, E> =
        cached ?: RealContainer<S, E>(initialState, parentScope).also { cached = it }

    override fun isInitialized() = cached != null
}