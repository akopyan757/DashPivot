package com.cheesecake.common.auth.service

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.verefication.VerificationError

interface UserService {
    suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<String, RegisterError>
    suspend fun verifyByToken(token: String?): ApiResult<String, VerificationError>
}