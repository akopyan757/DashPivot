package com.cheesecake.server.auth.route.repository

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.config.Config
import com.cheesecake.common.auth.model.error.AuthError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.registration.RegisterResponse
import com.cheesecake.common.auth.model.sendCode.SendCodeRequest
import com.cheesecake.common.auth.model.sendCode.SendCodeType
import com.cheesecake.common.auth.service.UserService
import com.cheesecake.common.auth.utils.isValidEmail
import com.cheesecake.common.auth.utils.isValidPassword
import com.cheesecake.server.auth.route.database.IUserSource
import com.cheesecake.server.auth.route.mail.IEmailService
import com.cheesecake.server.auth.route.utils.IPasswordHasher
import com.cheesecake.server.auth.route.utils.ITokenGenerator
import com.cheesecake.server.auth.route.utils.IVerifyCodeGenerator

internal class UserRepository(
    private val emailService: IEmailService,
    private val passwordHasher: IPasswordHasher,
    private val verifyCodeGenerator: IVerifyCodeGenerator,
    private val tokenGenerator: ITokenGenerator,
    private val userSource: IUserSource,
): UserService {
    override suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<RegisterResponse, AuthError> {
        if (userSource.isEmailTakenAndVerified(registerRequest.email)) {
            return ApiResult.Error(AuthError.EMAIL_TAKEN)
        }

        if (!isValidEmail(registerRequest.email)) {
            return ApiResult.Error(AuthError.INVALID_EMAIL_FORMAT)
        }

        if (!isValidPassword(registerRequest.password)) {
            return ApiResult.Error(AuthError.INVALID_PASSWORD)
        }

        if (!userSource.canSendVerificationCode(registerRequest.email, SendCodeType.REGISTRATION)) {
            return ApiResult.Error(AuthError.TOO_MANY_REQUESTS)
        }

        val hashedPassword = passwordHasher.hashPassword(registerRequest.password)
        val verificationCode = verifyCodeGenerator.generateVerificationCode(Config.VERIFICATION_CODE_COUNT)
        val hashedVerificationCode = passwordHasher.hashPassword(verificationCode)

        userSource.createUser(
            registerRequest.email, hashedPassword, isVerified = false, hashedVerificationCode
        )

        if (!emailService.sendVerificationEmail(
            registerRequest.email, verificationCode, SendCodeType.REGISTRATION
        )) {
            return ApiResult.Error(AuthError.VERIFICATION_LETTER_SENDING_ERROR)
        }

        return ApiResult.Success(RegisterResponse(registerRequest.email))
    }

    override suspend fun verifyEmailByCode(email: String, code: String): ApiResult<String, AuthError> {
        if (code.isBlank()) {
            return ApiResult.Error(AuthError.EMPTY_CODE_ERROR)
        }

        val user = userSource.findUserForVerification(email, SendCodeType.REGISTRATION)
            ?: return ApiResult.Error(AuthError.USER_NOT_FOUND)

        val userCode = user.verificationHashedCode
        if (userCode.isNullOrBlank()) {
            return ApiResult.Error(AuthError.VERIFICATION_CODE_NOT_FOUND)
        }

        if (!passwordHasher.verifyPassword(code, userCode)) {
            return ApiResult.Error(AuthError.EXPIRED_CODE)
        }

        userSource.verifyEmail(user.id)

        return ApiResult.Success("Email confirmed successfully!")
    }


    override suspend fun sendCode(request: SendCodeRequest): ApiResult<String, AuthError> {
        val email = request.email
        val operationType = request.type
        val user = userSource.findUserByEmail(email) ?: run {
            return ApiResult.Error(AuthError.USER_NOT_FOUND)
        }

        val isVerified = userSource.isEmailTakenAndVerified(email)
        if (operationType == SendCodeType.REGISTRATION) {
            if (isVerified) {
                return ApiResult.Error(AuthError.EMAIL_ALREADY_VERIFIED)
            }
        } else if (operationType == SendCodeType.RESET_PASSWORD) {
            if (!isVerified) {
                return ApiResult.Error(AuthError.USER_NOT_VERIFIED)
            }
        }

        if (!userSource.canSendVerificationCode(email, operationType)) {
            return ApiResult.Error(AuthError.TOO_MANY_REQUESTS)
        }

        val verificationCode = verifyCodeGenerator.generateVerificationCode(
            Config.VERIFICATION_CODE_COUNT
        )
        val hashedVerificationCode = passwordHasher.hashPassword(verificationCode)

        userSource.insertAndDeleteVerificationCode(user.id, hashedVerificationCode, operationType)

        if (!emailService.sendVerificationEmail(email, verificationCode, operationType)) {
            return ApiResult.Error(AuthError.EMAIL_SENDING_FAILED)
        }

        return ApiResult.Success("Code sent successfully")
    }

    override suspend fun resetPassword(
        email: String,
        code: String,
        newPassword: String
    ): ApiResult<String, AuthError> {
        val user = userSource.findUserForVerification(email, SendCodeType.RESET_PASSWORD)
            ?: run { return ApiResult.Error(AuthError.USER_NOT_FOUND) }

        val oldPasswordHash = user.passwordHash ?: run {
            return ApiResult.Error(AuthError.USER_NOT_FOUND)
        }

        if (!userSource.isEmailTakenAndVerified(email)) {
            return ApiResult.Error(AuthError.USER_NOT_VERIFIED)
        }

        val userCode = user.verificationHashedCode
        if (userCode.isNullOrBlank()) {
            return ApiResult.Error(AuthError.VERIFICATION_CODE_NOT_FOUND)
        }

        if (!passwordHasher.verifyPassword(code, userCode)) {
            return ApiResult.Error(AuthError.EXPIRED_CODE)
        }

        if (passwordHasher.verifyPassword(newPassword, oldPasswordHash)) {
            return ApiResult.Error(AuthError.SAME_PASSWORD)
        }

        val newHashedPassword = passwordHasher.hashPassword(newPassword)
        userSource.changePassword(user.id, newHashedPassword)

        return ApiResult.Success("Password was changed successfully")

    }

    override suspend fun loginUser(loginRequest: LoginRequest): ApiResult<String, AuthError> {
        val user = userSource.findUserByEmail(loginRequest.email)
            ?: return ApiResult.Error(AuthError.USER_NOT_FOUND)

        if (!passwordHasher.verifyPassword(loginRequest.password, user.passwordHash)) {
            return ApiResult.Error(AuthError.INVALID_PASSWORD)
        }

        if (!user.isVerified) {
            return ApiResult.Error(AuthError.EMAIL_NOT_VERIFIED)
        }

        val token = tokenGenerator.generateToken(user.id.toString())

        return ApiResult.Success(token)
    }
}