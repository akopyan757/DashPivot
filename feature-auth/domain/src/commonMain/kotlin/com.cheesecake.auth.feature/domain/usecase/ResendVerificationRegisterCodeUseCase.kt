package com.cheesecake.auth.feature.domain.usecase

import com.cheesecake.auth.feature.domain.repository.IUserRepository
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.sendCode.SendCodeError
import com.cheesecake.common.auth.model.sendCode.SendCodeType
import kotlinx.coroutines.flow.Flow

class ResendVerificationRegisterCodeUseCase(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(email: String): Flow<ApiResult<String, SendCodeError>> {
        return userRepository.sendVerificationCode(email, SendCodeType.REGISTRATION)
    }
}