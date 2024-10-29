package com.cheesecake.auth.data.network

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

expect fun getPlatformEngine(): HttpClientEngineFactory<HttpClientEngineConfig>