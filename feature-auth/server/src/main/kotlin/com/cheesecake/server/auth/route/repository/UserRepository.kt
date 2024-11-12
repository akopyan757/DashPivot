package com.cheesecake.server.auth.route.repository

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.config.Config
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.resendCode.ResendCodeError
import com.cheesecake.common.auth.model.verefication.VerificationError
import com.cheesecake.common.auth.service.UserService
import com.cheesecake.common.auth.utils.isValidEmail
import com.cheesecake.common.auth.utils.isValidPassword
import com.cheesecake.server.auth.route.database.IUserSource
import com.cheesecake.server.auth.route.mail.IEmailService
import com.cheesecake.server.auth.route.utils.IPasswordHasher
import com.cheesecake.server.auth.route.utils.ITokenGenerator
import com.cheesecake.server.auth.route.utils.IVerifyCodeGenerator
import org.jetbrains.exposed.sql.transactions.transaction

internal class UserRepository(
    private val emailService: IEmailService,
    private val passwordHasher: IPasswordHasher,
    private val verifyCodeGenerator: IVerifyCodeGenerator,
    private val tokenGenerator: ITokenGenerator,
    private val userSource: IUserSource,
): UserService {
    override suspend fun registerUser(registerRequest: RegisterRequest): ApiResult<String, RegisterError> {
        if (userSource.isEmailTakenAndVerified(registerRequest.email)) {
            return ApiResult.Error(RegisterError.EMAIL_TAKEN)
        }

        if (!isValidEmail(registerRequest.email)) {
            return ApiResult.Error(RegisterError.INVALID_EMAIL_FORMAT)
        }

        if (!isValidPassword(registerRequest.password)) {
            return ApiResult.Error(RegisterError.INVALID_PASSWORD)
        }

        if (!userSource.canSendVerificationCode(registerRequest.email)) {
            return ApiResult.Error(RegisterError.TOO_MANY_REQUESTS)
        }

        val hashedPassword = passwordHasher.hashPassword(registerRequest.password)
        val verificationCode = verifyCodeGenerator.generateVerificationCode(Config.VERIFICATION_CODE_COUNT)
        val hashedVerificationCode = passwordHasher.hashPassword(verificationCode)

        userSource.createUser(
            registerRequest.email, hashedPassword, isVerified = false, hashedVerificationCode
        )

        if (!emailService.sendVerificationEmail(registerRequest.email, verificationCode)) {
            return ApiResult.Error(RegisterError.VERIFICATION_LETTER_SENDING_ERROR)
        }

        return ApiResult.Success("User registered successfully")
    }

    override suspend fun verifyEmailByCode(email: String, code: String?): ApiResult<String, VerificationError> {
        if (code.isNullOrBlank()) {
            return ApiResult.Error(VerificationError.EMPTY_CODE_ERROR)
        }

        val user = userSource.findUserForVerification(email)
            ?: return ApiResult.Error(VerificationError.USER_NOT_FOUND)

        val userCode = user.verificationHashedCode
        if (userCode.isNullOrBlank()) {
            return ApiResult.Error(VerificationError.VERIFICATION_CODE_NOT_FOUND)
        }

        if (!passwordHasher.verifyPassword(code, userCode)) {
            return ApiResult.Error(VerificationError.EXPIRED_CODE)
        }

        userSource.verifyEmail(user.id)

        return ApiResult.Success("Email confirmed successfully!")
    }


    override suspend fun resendCode(email: String): ApiResult<String, ResendCodeError> {
        val user = userSource.findUserByEmail(email) ?: run {
            return ApiResult.Error(ResendCodeError.USER_NOT_FOUND)
        }

        if (userSource.isEmailTakenAndVerified(email)) {
            return ApiResult.Error(ResendCodeError.EMAIL_ALREADY_VERIFIED)
        }

        if (!userSource.canSendVerificationCode(email)) {
            return ApiResult.Error(ResendCodeError.TOO_MANY_REQUESTS)
        }

        val verificationCode = verifyCodeGenerator.generateVerificationCode(Config.VERIFICATION_CODE_COUNT)
        val hashedVerificationCode = passwordHasher.hashPassword(verificationCode)

        userSource.updateVerificationCode(user.id, hashedVerificationCode)

        if (!emailService.sendVerificationEmail(email, verificationCode)) {
            return ApiResult.Error(ResendCodeError.EMAIL_SENDING_FAILED)
        }

        return ApiResult.Success("Verification code sent successfully")
    }

    override suspend fun loginUser(loginRequest: LoginRequest): ApiResult<String, LoginError> {
        val user = userSource.findUserByEmail(loginRequest.email)
            ?: return ApiResult.Error(LoginError.USER_NOT_FOUND)

        if (!passwordHasher.verifyPassword(loginRequest.password, user.passwordHash)) {
            return ApiResult.Error(LoginError.INVALID_PASSWORD)
        }

        if (!user.isVerified) {
            return ApiResult.Error(LoginError.EMAIL_NOT_VERIFIED)
        }

        val token = tokenGenerator.generateToken(user.id.toString())

        return ApiResult.Success(token)
    }
}