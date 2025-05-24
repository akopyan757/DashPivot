package com.cheesecake.auth.data.source

import com.cheesecake.auth.data.service.ApiService
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.api.Log
import com.cheesecake.common.api.RequestHandler
import com.cheesecake.common.auth.model.changePassword.ResetPasswordRequest
import com.cheesecake.common.auth.model.error.AuthError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.registration.RegisterResponse
import com.cheesecake.common.auth.model.sendCode.SendCodeRequest
import com.cheesecake.common.auth.model.verefication.VerificationRequest
import kotlinx.serialization.builtins.serializer

class UserRemoteDataSource(
    private val apiService: ApiService,
    private val requestHandler: RequestHandler,
): IUserRemoteDataSource {

    override suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<RegisterResponse, AuthError> {
        return requestHandler.execute(
            request = { apiService.registerUser(registerRequest) },
            serializer = RegisterResponse.serializer(),
            onError = ::errorFromCode,
            onException = ::defaultError,
        )
    }

    override suspend fun verifyEmailByCode(
        verificationRequest: VerificationRequest
    ): ApiResult<String, AuthError> {
        return requestHandler.execute(
            request = { apiService.verificationCode(verificationRequest) },
            serializer = String.serializer(),
            onError = ::errorFromCode,
            onException = ::defaultError,
        )
    }

    override suspend fun sendCode(request: SendCodeRequest): ApiResult<String, AuthError> {
        return requestHandler.execute(
            request = { apiService.sendCode(request) },
            serializer = String.serializer(),
            onError = ::errorFromCode,
            onException = ::defaultError,
        )
    }

    override suspend fun resetPassword(
        resetPasswordRequest: ResetPasswordRequest
    ): ApiResult<String, AuthError> {
        return requestHandler.execute(
            request = { apiService.resetPassword(resetPasswordRequest) },
            serializer = String.serializer(),
            onError = ::errorFromCode,
            onException = ::defaultError,
        )
    }

    override suspend fun loginUser(loginRequest: LoginRequest): ApiResult<String, AuthError> {
        return requestHandler.execute(
            request = { apiService.loginUser(loginRequest) },
            serializer = String.serializer(),
            onError = ::errorFromCode,
            onException = ::defaultError,
        )
    }

    private inline fun errorFromCode(code: Int, body: String): AuthError {
        return enumValues<AuthError>().firstOrNull { it.code == code && it.message == body }
            ?: AuthError.UNKNOWN
    }

    private inline fun defaultError(exception: Exception): AuthError {
        Log.error("UserRemoteDataSource", exception)
        return AuthError.UNKNOWN
    }
}