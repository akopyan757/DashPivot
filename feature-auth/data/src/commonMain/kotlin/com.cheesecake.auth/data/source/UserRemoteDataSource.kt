package com.cheesecake.auth.data.source

import com.cheesecake.auth.data.service.ApiService
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.api.RequestHandler
import com.cheesecake.common.auth.model.changePassword.ResetPasswordError
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.sendCode.SendCodeError
import com.cheesecake.common.auth.model.sendCode.SendCodeRequest
import com.cheesecake.common.auth.model.verefication.VerificationError

class UserRemoteDataSource(
    private val apiService: ApiService,
    private val requestHandler: RequestHandler,
): IUserRemoteDataSource {

    override suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<String, RegisterError> {
        return requestHandler.execute(
            request = { apiService.registerUser(registerRequest) },
            onSuccess = { it },
            onError = { RegisterError.fromMessage(it) }
        )
    }

    override suspend fun verifyEmailByCode(
        email: String,
        code: String
    ): ApiResult<String, VerificationError> {
        return requestHandler.execute(
            request = { apiService.verificationCode(email, code) },
            onSuccess = { it },
            onError = { VerificationError.fromMessage(it) }
        )
    }

    override suspend fun sendCode(request: SendCodeRequest): ApiResult<String, SendCodeError> {
        return requestHandler.execute(
            request = { apiService.sendCode(request) },
            onSuccess = { it },
            onError = { SendCodeError.fromMessage(it) }
        )
    }

    override suspend fun resetPassword(
        email: String,
        code: String,
        newPassword: String
    ): ApiResult<String, ResetPasswordError> {
        return requestHandler.execute(
            request = { apiService.resetPassword(email, code, newPassword) },
            onSuccess = { it },
            onError = { ResetPasswordError.fromMessage(it) }
        )
    }

    override suspend fun loginUser(loginRequest: LoginRequest): ApiResult<String, LoginError> {
        return requestHandler.execute(
            request = { apiService.loginUser(loginRequest) },
            onSuccess = { it },
            onError = { LoginError.fromMessage(it) }
        )
    }
}