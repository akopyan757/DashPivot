package com.cheesecake.auth.feature.domain.usecase

import com.cheesecake.auth.feature.domain.repository.IUserRepository
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.error.AuthError
import kotlinx.coroutines.flow.Flow

class ResetPasswordUseCase(
    private val userRepository: IUserRepository
) {
    operator fun invoke(
        email: String, code: String, password: String
    ): Flow<ApiResult<String, AuthError>> {
        return userRepository.resetPassword(email, code, password)
    }
}