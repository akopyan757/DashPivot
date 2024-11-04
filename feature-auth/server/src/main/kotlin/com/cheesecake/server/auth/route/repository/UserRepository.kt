package com.cheesecake.server.auth.route.repository

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.verefication.VerificationError
import com.cheesecake.common.auth.service.UserService
import com.cheesecake.common.auth.utils.isValidEmail
import com.cheesecake.common.auth.utils.isValidPassword
import com.cheesecake.server.auth.route.database.UserSource
import com.cheesecake.server.auth.route.mail.IEmailService
import com.cheesecake.server.auth.route.utils.PasswordHasher
import com.cheesecake.server.auth.route.utils.TokenUtils
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

    override suspend fun verifyByToken(token: String?): ApiResult<String, VerificationError> {
        if (token.isNullOrBlank()) {
            return ApiResult.Error(VerificationError.EMPTY_TOKEN_ERROR)
        }

        val user = UserSource.findUserByToken(token)
            ?: return ApiResult.Error(VerificationError.EXPIRED_TOKEN)

        UserSource.verifyEmail(user.id)

        return ApiResult.Success("Email confirmed successfully!")
    }

    override suspend fun loginUser(loginRequest: LoginRequest): ApiResult<String, LoginError> {
        val user = UserSource.findUserByEmail(loginRequest.email)
            ?: return ApiResult.Error(LoginError.USER_NOT_FOUND)

        if (!PasswordHasher.verifyPassword(loginRequest.password, user.passwordHash)) {
            return ApiResult.Error(LoginError.INVALID_PASSWORD)
        }

        if (!user.isVerified) {
            return ApiResult.Error(LoginError.EMAIL_NOT_VERIFIED)
        }

        val token = TokenUtils.generateToken(user.id.toString())

        return ApiResult.Success(token)
    }
}