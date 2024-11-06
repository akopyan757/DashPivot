package com.cheesecake.common.ui.navigator.state

import kotlin.reflect.KClass

interface IStateManager {
    fun getStringState(key: String): String?
    fun setStringState(key: String, value: String)
    fun <T: Any> getSerializableState(key: String, kClass: KClass<T>): T?
    fun <T: Any> setSerializableState(key: String, value: T, kClass: KClass<T>)
}