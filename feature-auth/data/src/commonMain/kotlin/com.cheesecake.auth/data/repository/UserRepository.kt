package com.cheesecake.auth.data.repository

import com.cheesecake.auth.data.source.IUserRemoteDataSource
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.RegisterRequest
import com.cheesecake.common.auth.model.VerificationError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class UserRepository(
    private val userRemoteDataSource: IUserRemoteDataSource
): IUserRepository {
    override suspend fun registerUser(
        email: String,
        password: String
    ) = flow {
        emit(userRemoteDataSource.registerUser(RegisterRequest(email, password)))
        println("registerUser: end")
    }.flowOn(Dispatchers.IO)

    override suspend fun verifyUserToken(token: String): Flow<ApiResult<String, VerificationError>> = flow {
        println("VerificationScreen: UserRepository: start: $token")
        delay(5000)
        emit(ApiResult.Success("Account verified!"))
        println("VerificationScreen: UserRepository: end: $token")
    }.flowOn(Dispatchers.IO)
}