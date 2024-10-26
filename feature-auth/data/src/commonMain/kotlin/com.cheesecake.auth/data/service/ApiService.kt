package com.cheesecake.auth.data.service

import com.cheesecake.common.auth.model.RegisterRequest
import io.ktor.client.statement.HttpResponse


interface ApiService {
    suspend fun registerUser(request: RegisterRequest): HttpResponse
}