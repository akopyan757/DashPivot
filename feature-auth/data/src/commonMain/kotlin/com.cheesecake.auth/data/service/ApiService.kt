package com.cheesecake.auth.data.service

import com.cheesecake.common.auth.model.registration.RegisterRequest
import io.ktor.client.statement.HttpResponse


interface ApiService {
    suspend fun registerUser(request: RegisterRequest): HttpResponse
    suspend fun verificationToken(token: String): HttpResponse
}