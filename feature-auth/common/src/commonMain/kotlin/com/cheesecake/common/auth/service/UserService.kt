package com.cheesecake.common.auth.service

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.changePassword.ResetPasswordRequest
import com.cheesecake.common.auth.model.error.AuthError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.registration.RegisterResponse
import com.cheesecake.common.auth.model.sendCode.SendCodeRequest
import com.cheesecake.common.auth.model.verefication.VerificationRequest

interface UserService {
    suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<RegisterResponse, AuthError>
    suspend fun verifyEmailByCode(verificationRequest: VerificationRequest): ApiResult<String, AuthError>
    suspend fun sendCode(request: SendCodeRequest): ApiResult<String, AuthError>
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): ApiResult<String, AuthError>
    suspend fun loginUser(loginRequest: LoginRequest): ApiResult<String, AuthError>
}