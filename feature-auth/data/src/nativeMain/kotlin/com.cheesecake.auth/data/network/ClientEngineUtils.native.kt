package com.cheesecake.auth.data.network

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.* // для iOS

actual fun getPlatformEngine(): HttpClientEngineFactory<HttpClientEngineConfig> = Darwin