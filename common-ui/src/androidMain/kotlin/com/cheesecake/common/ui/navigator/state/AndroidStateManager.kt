package com.cheesecake.common.ui.navigator.state

import androidx.lifecycle.SavedStateHandle
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

class AndroidStateManager(
    private val savedStateHandle: SavedStateHandle,
    private val classSerializers: IKClassSerializers,
) : IStateManager {

    private val json = Json { ignoreUnknownKeys = true }

    override fun getStringState(key: String): String? {
        return savedStateHandle.get<String>(key)
    }

    override fun setStringState(key: String, value: String) {
        savedStateHandle[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getSerializableState(key: String, kClass: KClass<T>): T? {
        val jsonString = savedStateHandle.get<String>(key) ?: return null
        val serializer = classSerializers.serializers[kClass] ?: return null
        return json.decodeFromString(serializer, jsonString) as? T
    }

    override fun <T : Any> setSerializableState(key: String, value: T, kClass: KClass<T>) {
        val serializer = classSerializers.serializers[kClass] as? KSerializer<T> ?: return
        val jsonString = json.encodeToString(serializer, value)
        savedStateHandle[key] = jsonString
    }
}