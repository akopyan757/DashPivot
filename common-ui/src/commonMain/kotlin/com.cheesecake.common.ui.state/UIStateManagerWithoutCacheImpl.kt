package com.cheesecake.common.ui.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class UIStateManagerWithoutCacheImpl<T>(
    private val serializer: KSerializer<T>
): UIStateManager<T> where T: UIState {

    private var _state: MutableStateFlow<T> = MutableStateFlow(createDefaultObject(serializer))
    override val state: StateFlow<T>  = _state

    override fun update(transform: T.() -> T) {
        _state.value = _state.value.transform()
    }

    override fun clear() {
         _state.value = createDefaultObject(serializer)
    }

    private fun <T> createDefaultObject(serializer: KSerializer<T>): T {
        return Json.decodeFromString(serializer, "{}")
    }
}


