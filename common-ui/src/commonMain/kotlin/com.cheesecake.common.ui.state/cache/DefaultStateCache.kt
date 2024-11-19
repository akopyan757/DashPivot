package com.cheesecake.common.ui.state.cache

import kotlinx.serialization.KSerializer

class DefaultStateCache : StateCache {
    private val sharedStringStates = mutableMapOf<String, String>()
    private val sharedSerializableStates = mutableMapOf<String, Any>()

    override fun getStringState(key: String): String? {
        return sharedStringStates[key]
    }

    override fun setStringState(key: String, value: String) {
        sharedStringStates[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getSerializableState(key: String, kSerializer: KSerializer<T>): T? {
        return sharedSerializableStates[key] as? T
    }

    override fun <T : Any> setSerializableState(
        key: String,
        value: T,
        kSerializer: KSerializer<T>
    ) {
        sharedSerializableStates[key] = value
    }
}