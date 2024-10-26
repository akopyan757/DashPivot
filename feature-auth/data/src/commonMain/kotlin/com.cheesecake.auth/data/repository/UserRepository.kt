package com.cheesecake.auth.data.repository

import com.cheesecake.auth.data.source.IUserRemoteDataSource
import com.cheesecake.common.auth.model.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
}