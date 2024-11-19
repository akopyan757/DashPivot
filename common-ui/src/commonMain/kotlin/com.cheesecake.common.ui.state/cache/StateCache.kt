package com.cheesecake.common.ui.state.cache

import kotlinx.serialization.KSerializer

interface StateCache {
    fun getStringState(key: String): String?
    fun setStringState(key: String, value: String)
    fun <T: Any> getSerializableState(key: String, kSerializer: KSerializer<T>): T?
    fun <T: Any> setSerializableState(key: String, value: T, kSerializer: KSerializer<T>)
}