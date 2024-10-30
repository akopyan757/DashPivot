package com.cheesecake.auth.data.repository

import com.cheesecake.auth.data.source.IUserRemoteDataSource
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.verefication.VerificationError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(
    private val userRemoteDataSource: IUserRemoteDataSource
): IUserRepository {
    override suspend fun registerUser(
        email: String,
        password: String
    ) = flow {
        emit(userRemoteDataSource.registerUser(RegisterRequest(email, password)))
    }.flowOn(Dispatchers.IO)

    override suspend fun verifyUserToken(token: String): Flow<ApiResult<String, VerificationError>> = flow {
        emit(userRemoteDataSource.verifyByToken(token))
    }.flowOn(Dispatchers.IO)
}