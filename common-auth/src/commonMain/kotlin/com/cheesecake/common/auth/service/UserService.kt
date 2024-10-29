package com.cheesecake.common.auth.service

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.RegisterError
import com.cheesecake.common.auth.model.RegisterRequest
import com.cheesecake.common.auth.model.VerificationError

interface UserService {
    suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<String, RegisterError>
    suspend fun verifyByToken(token: String?): ApiResult<String, VerificationError>
}