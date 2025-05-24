package com.cheesecake.auth.feature.domain.usecase

import com.cheesecake.auth.feature.domain.repository.IUserRepository
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.error.AuthError
import com.cheesecake.common.auth.model.registration.RegisterResponse
import kotlinx.coroutines.flow.Flow

class RegisterUseCase(
    private val userRepository: IUserRepository
) {
    operator fun invoke(email: String, password: String): Flow<ApiResult<RegisterResponse, AuthError>> {
        return userRepository.registerUser(email, password)
    }
}