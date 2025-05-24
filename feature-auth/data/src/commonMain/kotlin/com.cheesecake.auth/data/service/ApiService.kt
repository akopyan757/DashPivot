package com.cheesecake.auth.data.service

import com.cheesecake.common.auth.model.changePassword.ResetPasswordRequest
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.sendCode.SendCodeRequest
import com.cheesecake.common.auth.model.verefication.VerificationRequest
import io.ktor.client.statement.HttpResponse


interface ApiService {
    suspend fun registerUser(request: RegisterRequest): HttpResponse
    suspend fun verificationCode(verificationRequest: VerificationRequest): HttpResponse
    suspend fun sendCode(request: SendCodeRequest): HttpResponse
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): HttpResponse
    suspend fun loginUser(request: LoginRequest): HttpResponse
}