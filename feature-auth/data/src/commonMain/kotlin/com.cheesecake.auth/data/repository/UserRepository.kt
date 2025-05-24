package com.cheesecake.auth.data.repository

import com.cheesecake.auth.data.source.IUserRemoteDataSource
import com.cheesecake.auth.feature.domain.repository.IUserRepository
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.changePassword.ResetPasswordRequest
import com.cheesecake.common.auth.model.error.AuthError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.sendCode.SendCodeRequest
import com.cheesecake.common.auth.model.sendCode.SendCodeType
import com.cheesecake.common.auth.model.verefication.VerificationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(
    private val userRemoteDataSource: IUserRemoteDataSource
): IUserRepository {
    override fun registerUser(
        email: String,
        password: String,
    ) = flow {
        emit(userRemoteDataSource.registerUser(RegisterRequest(email, password)))
    }.flowOn(Dispatchers.IO)

    override fun verifyEmailByCode(email: String, code: String) = flow {
        emit(userRemoteDataSource.verifyEmailByCode(VerificationRequest(email, code)))
    }.flowOn(Dispatchers.IO)

    override fun sendVerificationCode(
        email: String,
        sendCodeType: SendCodeType,
    ): Flow<ApiResult<String, AuthError>> = flow {
        emit(userRemoteDataSource.sendCode(SendCodeRequest(email, sendCodeType)))
    }.flowOn(Dispatchers.IO)

    override fun resetPassword(
        email: String,
        code: String,
        password: String
    ): Flow<ApiResult<String, AuthError>> = flow {
        emit(userRemoteDataSource.resetPassword(ResetPasswordRequest(email, code, password)))
    }.flowOn(Dispatchers.IO)

    override fun loginUser(
        email: String,
        password: String
    ): Flow<ApiResult<String, AuthError>> = flow {
        emit(userRemoteDataSource.loginUser(LoginRequest(email, password)))
    }.flowOn(Dispatchers.IO)
}