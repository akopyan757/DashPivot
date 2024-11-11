package com.cheesecake.common.ui.events

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventStateHolder {

    private val _state = MutableStateFlow<Map<String, String>>(emptyMap())
    val state: StateFlow<Map<String, String>> = _state

    fun put(key: String, value: String) {
        println("EventStateHolder: put: key = $key, value=$value")
        _state.value += (key to value)
    }

    // Метод для получения значения по ключу
    fun get(key: String): String? {
        return _state.value[key].also {
            println("EventStateHolder: get: key = $key, value=$it")
        }
    }

    // Метод для удаления значения по ключу
    fun getAndRemove(key: String): String? {
        return get(key)?.also {
            _state.value -= key
            println("EventStateHolder: remove: key = $key")
        }
    }
}