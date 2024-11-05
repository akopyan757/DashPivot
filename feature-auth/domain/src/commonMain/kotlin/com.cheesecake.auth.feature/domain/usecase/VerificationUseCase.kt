package com.cheesecake.auth.feature.domain.usecase

import com.cheesecake.auth.feature.domain.repository.IUserRepository
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.verefication.VerificationError
import kotlinx.coroutines.flow.Flow

class VerificationUseCase(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(email: String, code: String): Flow<ApiResult<String, VerificationError>> {
        return userRepository.verifyEmailByCode(email, code)
    }
}