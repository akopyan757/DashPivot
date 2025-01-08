package com.cheesecake.auth.data.source

import com.cheesecake.auth.data.service.ApiService
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.sendCode.SendCodeType
import com.cheesecake.common.auth.model.sendCode.SendCodeError
import com.cheesecake.common.auth.model.verefication.VerificationError
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

class UserRemoteDataSource(private val apiService: ApiService): IUserRemoteDataSource {

    override suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<String, RegisterError> {
        return try {
            val response = apiService.registerUser(registerRequest)
            val statusCode = response.status.value
            if (HttpStatusCode.fromValue(statusCode).isSuccess()) {
                ApiResult.Success(response.bodyAsText())
            } else {
                ApiResult.Error(RegisterError.fromMessage(response.bodyAsText()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error(RegisterError.UNKNOWN)
        }
    }

    override suspend fun verifyEmailByCode(
        email: String,
        code: String?
    ): ApiResult<String, VerificationError> {
        return try {
            val response = apiService.verificationCode(email, code!!)
            val statusCode = response.status.value
            if (HttpStatusCode.fromValue(statusCode).isSuccess()) {
                ApiResult.Success(response.bodyAsText())
            } else {
                ApiResult.Error(VerificationError.fromMessage(response.bodyAsText()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error(VerificationError.UNKNOWN)
        }
    }

    override suspend fun sendVerificationCode(
        email: String,
        operationType: SendCodeType
    ): ApiResult<String, SendCodeError> {
        return try {
            val response = apiService.sendVerificationCode(email, operationType)
            val statusCode = response.status.value
            if (HttpStatusCode.fromValue(statusCode).isSuccess()) {
                ApiResult.Success(response.bodyAsText())
            } else {
                ApiResult.Error(SendCodeError.fromMessage(response.bodyAsText()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error(SendCodeError.UNKNOWN)
        }
    }

    override suspend fun loginUser(loginRequest: LoginRequest): ApiResult<String, LoginError> {
        return try {
            val response = apiService.loginUser(loginRequest)
            val statusCode = response.status.value
            if (HttpStatusCode.fromValue(statusCode).isSuccess()) {
                ApiResult.Success(response.bodyAsText())
            } else {
                ApiResult.Error(LoginError.fromMessage(response.bodyAsText()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error(LoginError.UNKNOWN)
        }
    }
}