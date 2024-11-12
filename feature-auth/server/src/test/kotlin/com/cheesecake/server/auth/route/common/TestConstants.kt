package com.cheesecake.server.auth.route.common

import com.cheesecake.common.auth.model.User
import com.cheesecake.server.auth.route.database.IUserSource
import com.cheesecake.server.auth.route.mail.IEmailService
import com.cheesecake.server.auth.route.utils.IPasswordHasher
import com.cheesecake.server.auth.route.utils.ITokenGenerator
import com.cheesecake.server.auth.route.utils.IVerifyCodeGenerator
import io.mockk.mockk
import java.time.LocalDateTime

object TestConstants {

    const val TEST_USER_ID = 1
    const val TEST_EMAIL = "test@example.com"
    const val TEST_EMAIL_NOT_VERIFIED = "test_not_verified@example.com"
    const val INVALID_EMAIL = "invalid-email"
    const val TEST_PASSWORD = "Zaq12345@"
    const val SHORT_PASSWORD = "short"
    const val HASHED_PASSWORD = "hashedPassword"
    const val VERIFICATION_CODE = "123456"
    const val HASHED_VERIFICATION_CODE = "hashedVerificationCode"
    const val SUCCESS_MESSAGE = "User registered successfully"
    const val TOKEN = "token"

    val TEST_CREATED_AT = LocalDateTime.now()
    val USER_VERIFIED = User(id = TEST_USER_ID, email = TEST_EMAIL, passwordHash = HASHED_PASSWORD, isVerified = true)
    val USER_NOT_VERIFIED = USER_VERIFIED.copy(isVerified = false)

    // Mocked dependencies
    val passwordHasher = mockk<IPasswordHasher>(relaxed = true)
    val tokenGenerator = mockk<ITokenGenerator>(relaxed = true)
    val verifyCodeGenerator = mockk<IVerifyCodeGenerator>(relaxed = true)
    val emailService = mockk<IEmailService>(relaxed = true)
    val userSource = mockk<IUserSource>(relaxed = true)

    fun clearMocks() {
        io.mockk.clearMocks(passwordHasher, tokenGenerator, verifyCodeGenerator, emailService, userSource)
    }
}