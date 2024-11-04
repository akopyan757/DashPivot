package com.cheesecake.auth.feature.domain.usecase

import com.cheesecake.auth.feature.domain.repository.IUserRepository
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.registration.RegisterError
import kotlinx.coroutines.flow.Flow

class RegisterUseCase(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<ApiResult<String, RegisterError>> {
        return userRepository.registerUser(email, password)
    }
}