package com.cheesecake.auth.data.di

import com.cheesecake.auth.data.network.getPlatformEngine
import com.cheesecake.auth.data.repository.IUserRepository
import com.cheesecake.auth.data.repository.UserRepository
import com.cheesecake.auth.data.service.ApiService
import com.cheesecake.auth.data.service.ApiServiceImpl
import com.cheesecake.auth.data.source.IUserRemoteDataSource
import com.cheesecake.auth.data.source.UserRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val authDataModule = module {
    single {
        HttpClient(getPlatformEngine()) {
            install(ContentNegotiation) {
                json(Json {
                    encodeDefaults = true
                })
            }
        }
    }
    single<ApiService> { ApiServiceImpl(get()) }
    single<IUserRemoteDataSource> { UserRemoteDataSource(get()) }
    single<IUserRepository> { UserRepository(get()) }
}