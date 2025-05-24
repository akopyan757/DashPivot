package com.cheesecake.auth.feature.domain.repository

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.error.AuthError
import com.cheesecake.common.auth.model.registration.RegisterResponse
import com.cheesecake.common.auth.model.sendCode.SendCodeType
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun registerUser(email: String, password: String): Flow<ApiResult<RegisterResponse, AuthError>>
    fun verifyEmailByCode(email: String, code: String): Flow<ApiResult<String, AuthError>>
    fun sendVerificationCode(email: String, sendCodeType: SendCodeType): Flow<ApiResult<String, AuthError>>
    fun resetPassword(email: String, code: String, password: String): Flow<ApiResult<String, AuthError>>
    fun loginUser(email: String, password: String): Flow<ApiResult<String, AuthError>>
}