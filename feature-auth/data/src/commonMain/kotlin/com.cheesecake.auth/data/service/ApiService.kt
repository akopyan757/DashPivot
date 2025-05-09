package com.cheesecake.auth.data.service

import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.sendCode.SendCodeRequest
import io.ktor.client.statement.HttpResponse


interface ApiService {
    suspend fun registerUser(request: RegisterRequest): HttpResponse
    suspend fun verificationCode(email: String, code: String): HttpResponse
    suspend fun sendCode(request: SendCodeRequest): HttpResponse
    suspend fun resetPassword(email: String, code: String, newPassword: String): HttpResponse

    suspend fun loginUser(request: LoginRequest): HttpResponse
}