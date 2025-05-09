package com.cheesecake.common.auth.service

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.error.AuthError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.sendCode.SendCodeRequest

interface UserService {
    suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<String, AuthError>
    suspend fun verifyEmailByCode(email: String, code: String): ApiResult<String, AuthError>
    suspend fun sendCode(request: SendCodeRequest): ApiResult<String, AuthError>
    suspend fun resetPassword(email: String, code: String, newPassword: String): ApiResult<String, AuthError>
    suspend fun loginUser(loginRequest: LoginRequest): ApiResult<String, AuthError>
}