package com.cheesecake.auth.feature.domain.usecase

import com.cheesecake.auth.feature.domain.repository.IUserRepository
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.login.LoginError
import kotlinx.coroutines.flow.Flow

class LoginUseCase(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<ApiResult<String, LoginError>> {
        return userRepository.loginUser(email, password)
    }
}