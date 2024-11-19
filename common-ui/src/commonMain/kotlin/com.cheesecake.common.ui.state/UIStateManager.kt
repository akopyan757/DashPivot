package com.cheesecake.common.ui.state

import kotlinx.coroutines.flow.StateFlow

interface UIStateManager<T> where T : UIState {
    val state: StateFlow<T>
    fun update(transform: T.() -> T)
    fun clear()
}