package com.cheesecake.auth.feature.domain.usecase

import com.cheesecake.auth.feature.domain.repository.IUserRepository
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.resendCode.ResendCodeError
import kotlinx.coroutines.flow.Flow

class ResendCodeUseCase(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(email: String): Flow<ApiResult<String, ResendCodeError>> {
        return userRepository.resendCode(email)
    }
}