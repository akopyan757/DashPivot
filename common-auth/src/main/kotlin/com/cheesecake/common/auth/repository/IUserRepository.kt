package com.cheesecake.common.auth.repository

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.RegisterError
import com.cheesecake.common.auth.model.RegisterRequest

interface IUserRepository {
    suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<String, RegisterError>
    suspend fun verifyByToken(token: String?): ApiResult<String, RegisterError>
}