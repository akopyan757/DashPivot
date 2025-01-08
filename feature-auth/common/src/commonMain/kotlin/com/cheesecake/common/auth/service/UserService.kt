package com.cheesecake.common.auth.service

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.changePassword.ChangePasswordError
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.sendCode.SendCodeType
import com.cheesecake.common.auth.model.sendCode.SendCodeError
import com.cheesecake.common.auth.model.verefication.VerificationError

interface UserService {
    suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<String, RegisterError>
    suspend fun verifyEmailByCode(email: String, code: String?): ApiResult<String, VerificationError>
    suspend fun sendVerificationCode(email: String, operationType: SendCodeType): ApiResult<String, SendCodeError>
    suspend fun loginUser(loginRequest: LoginRequest): ApiResult<String, LoginError>
    suspend fun changePassword(email: String, code: String, newHashedPassword: String): ApiResult<String, ChangePasswordError>
}