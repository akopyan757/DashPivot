package com.cheesecake.auth.data.repository

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.RegisterError
import com.cheesecake.common.auth.model.VerificationError
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun registerUser(email: String, password: String): Flow<ApiResult<String, RegisterError>>
    suspend fun verifyUserToken(token: String): Flow<ApiResult<String, VerificationError>>
}