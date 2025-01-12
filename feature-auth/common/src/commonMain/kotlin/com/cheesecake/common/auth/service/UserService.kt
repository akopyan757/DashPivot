package com.cheesecake.common.auth.service

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.changePassword.ResetPasswordError
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.sendCode.SendCodeError
import com.cheesecake.common.auth.model.verefication.VerificationError

interface UserService {
    suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<String, RegisterError>
    suspend fun verifyEmailByCode(email: String, code: String): ApiResult<String, VerificationError>
    suspend fun sendVerificationCode(email: String): ApiResult<String, SendCodeError>

    suspend fun sendPasswordCode(email: String): ApiResult<String, SendCodeError>
    suspend fun resetPassword(email: String, code: String, newPassword: String): ApiResult<String, ResetPasswordError>

    suspend fun loginUser(loginRequest: LoginRequest): ApiResult<String, LoginError>

}