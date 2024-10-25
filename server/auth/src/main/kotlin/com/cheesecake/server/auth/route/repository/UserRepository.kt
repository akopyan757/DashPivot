package com.cheesecake.server.auth.route.repository

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.RegisterError
import com.cheesecake.common.auth.model.RegisterRequest
import com.cheesecake.common.auth.service.UserService
import com.cheesecake.common.auth.utils.isValidEmail
import com.cheesecake.common.auth.utils.isValidPassword
import com.cheesecake.server.auth.route.database.UserSource
import com.cheesecake.server.auth.route.mail.IEmailService
import com.cheesecake.server.auth.route.utils.PasswordHasher
import com.cheesecake.server.auth.route.utils.VerificationUtils

class UserRepository(
    private val emailService: IEmailService
): UserService {
    override suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<String, RegisterError> {
        if (UserSource.isEmailTaken(registerRequest.email)) {
            return ApiResult.Error(RegisterError.EMAIL_TAKEN)
        }

        if (!isValidEmail(registerRequest.email)) {
            return ApiResult.Error(RegisterError.INVALID_PASSWORD)
        }

        if (!isValidPassword(registerRequest.password)) {
            return ApiResult.Error(RegisterError.INVALID_PASSWORD)
        }

        val hashedPassword = PasswordHasher.hashPassword(registerRequest.password)

        val verificationToken = VerificationUtils.generateVerificationToken()

        val user = UserSource.createUser(registerRequest.email, hashedPassword, isVerified = false, verificationToken)

        emailService.sendVerificationEmail(user.email, verificationToken)

        return ApiResult.Success("User registered successfully")
    }

    override suspend fun verifyByToken(token: String?): ApiResult<String, RegisterError> {
        if (token.isNullOrBlank()) {
            return ApiResult.Error(RegisterError.EMAIL_TAKEN)
        }

        val user = UserSource.findUserByToken(token)
            ?: return ApiResult.Error(RegisterError.EXPIRED_TOKEN)

        UserSource.verifyEmail(user.id)

        return ApiResult.Success("Email confirmed successfully!")
    }
}