package com.cheesecake.common.ui.events

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EventStateHolder<T : EventState> {

    private val _eventState = MutableStateFlow<T?>(null)
    val appEntryState = _eventState.asStateFlow()

    suspend fun emitEvent(event: T) {
        _eventState.emit(event)
    }
}