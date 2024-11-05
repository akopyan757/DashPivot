package com.cheesecake.common.auth.service

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.verefication.VerificationError

interface UserService {
    suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<String, RegisterError>
    @Deprecated("Use verifyByToken instead")
    suspend fun verifyByToken(token: String?): ApiResult<String, VerificationError>
    suspend fun verifyEmailByCode(code: String?): ApiResult<String, VerificationError>
    suspend fun loginUser(loginRequest: LoginRequest): ApiResult<String, LoginError>
}