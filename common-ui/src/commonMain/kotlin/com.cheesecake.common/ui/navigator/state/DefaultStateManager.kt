package com.cheesecake.common.ui.navigator.state

import kotlin.reflect.KClass

class DefaultStateManager : IStateManager {
    private val sharedStringStates = mutableMapOf<String, String>()
    private val sharedSerializableStates = mutableMapOf<String, Any>()

    override fun getStringState(key: String): String? {
        return sharedStringStates[key]
    }

    override fun setStringState(key: String, value: String) {
        sharedStringStates[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getSerializableState(key: String, kClass: KClass<T>): T? {
        return sharedSerializableStates[key] as? T
    }

    override fun <T : Any> setSerializableState(key: String, value: T, kClass: KClass<T>) {
        sharedSerializableStates[key] = value
    }
}