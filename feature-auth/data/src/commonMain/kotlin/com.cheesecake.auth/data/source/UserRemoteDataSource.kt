package com.cheesecake.auth.data.source

import com.cheesecake.auth.data.service.ApiService
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.RegisterError
import com.cheesecake.common.auth.model.RegisterRequest
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

    override suspend fun verifyByToken(token: String?): ApiResult<String, RegisterError> {
        TODO("Not yet implemented")
    }
}