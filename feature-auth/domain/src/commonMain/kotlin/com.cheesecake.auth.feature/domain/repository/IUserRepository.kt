package com.cheesecake.auth.feature.domain.repository

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.model.sendCode.SendCodeError
import com.cheesecake.common.auth.model.sendCode.SendCodeType
import com.cheesecake.common.auth.model.verefication.VerificationError
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun registerUser(email: String, password: String): Flow<ApiResult<String, RegisterError>>
    suspend fun verifyEmailByCode(email: String, code: String): Flow<ApiResult<String, VerificationError>>
    suspend fun sendVerificationCode(email: String, sendCodeType: SendCodeType): Flow<ApiResult<String, SendCodeError>>
    suspend fun loginUser(email: String, password: String): Flow<ApiResult<String, LoginError>>
}