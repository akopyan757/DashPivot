package com.cheesecake.common.ui.navigator.state

import androidx.lifecycle.SavedStateHandle
import com.cheesecake.common.ui.state.cache.StateCache
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class AndroidStateCache(
    private val savedStateHandle: SavedStateHandle,
) : StateCache {

    private val json = Json { ignoreUnknownKeys = true }

    override fun getStringState(key: String): String? {
        return savedStateHandle.get<String>(key)
    }

    override fun setStringState(key: String, value: String) {
        savedStateHandle[key] = value
    }

    override fun <T : Any> getSerializableState(key: String, kSerializer: KSerializer<T>): T? {
        val jsonString = savedStateHandle.get<String>(key) ?: return null
        return json.decodeFromString(kSerializer, jsonString) as? T
    }

    override fun <T : Any> setSerializableState(
        key: String,
        value: T,
        kSerializer: KSerializer<T>
    ) {
        savedStateHandle[key] = json.encodeToString(kSerializer, value)
    }
}