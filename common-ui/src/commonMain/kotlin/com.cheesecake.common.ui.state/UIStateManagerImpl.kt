package com.cheesecake.common.ui.state

import com.cheesecake.common.ui.state.cache.StateCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class UIStateManagerImpl<T>(
    private val stateManager: StateCache,
    private val stateKey: String,
    private val serializer: KSerializer<T>,
): UIStateManager<T> where T: UIState {

    private var _state: MutableStateFlow<T> = createMutableState(stateKey, serializer)
    override val state: StateFlow<T>  = _state

    override fun update(transform: T.() -> T) {
        val newState = _state.value.transform()
        _state.value = newState
        stateManager.setSerializableState(stateKey, newState, serializer)
    }

    override fun clear() {
        val newState = createDefaultObject(serializer)
        _state.value = newState
        stateManager.setSerializableState(stateKey, newState, serializer)
    }

    private fun createMutableState(key: String, serializer: KSerializer<T>): MutableStateFlow<T> {
        val lastCacheState = stateManager.getSerializableState(key, serializer)
        val initState = lastCacheState ?: createDefaultObject(serializer)
        val state = MutableStateFlow(initState)
        return state
    }

    private fun <T> createDefaultObject(serializer: KSerializer<T>): T {
        return Json.decodeFromString(serializer, "{}")
    }
}


