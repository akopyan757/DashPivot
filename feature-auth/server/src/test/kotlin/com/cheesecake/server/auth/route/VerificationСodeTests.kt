package com.cheesecake.server.auth.route

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.UserVerify
import com.cheesecake.common.auth.model.error.AuthError
import com.cheesecake.common.auth.model.sendCode.SendCodeType
import com.cheesecake.common.auth.service.UserService
import com.cheesecake.server.auth.route.common.TestConstants
import com.cheesecake.server.auth.route.repository.UserRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class VerificationСodeTests {

    private lateinit var userService: UserService

    @BeforeTest
    fun setup() {
        userService = UserRepository(
            TestConstants.emailService,
            TestConstants.passwordHasher,
            TestConstants.verifyCodeGenerator,
            TestConstants.tokenGenerator,
            TestConstants.userSource
        )
    }

    @Test
    fun `verifyEmailByCode returns EMPTY_CODE_ERROR when code is null or blank`() = runBlocking {
        val result = userService.verifyEmailByCode(TestConstants.TEST_EMAIL, "")
        assertEquals(ApiResult.Error(AuthError.EMPTY_CODE_ERROR), result)
    }

    @Test
    fun `verifyEmailByCode returns USER_NOT_FOUND when user is not found`() = runBlocking {
        every { TestConstants.userSource.findUserForVerification(TestConstants.TEST_EMAIL, SendCodeType.REGISTRATION) } returns null

        val result = userService.verifyEmailByCode(TestConstants.TEST_EMAIL, TestConstants.VERIFICATION_CODE)
        assertEquals(ApiResult.Error(AuthError.USER_NOT_FOUND), result)

        verify { TestConstants.userSource.findUserForVerification(TestConstants.TEST_EMAIL, SendCodeType.REGISTRATION) }
    }

    @Test
    fun `verifyEmailByCode returns VERIFICATION_CODE_NOT_FOUND when verification code is blank`() = runBlocking {
        every { TestConstants.userSource.findUserForVerification(TestConstants.TEST_EMAIL, SendCodeType.REGISTRATION) } returns UserVerify(
            id = 1,
            email = TestConstants.TEST_EMAIL,
            passwordHash = null,
            verificationHashedCode = null,
            createdAt = null,
        )

        val result = userService.verifyEmailByCode(TestConstants.TEST_EMAIL, TestConstants.VERIFICATION_CODE)
        assertEquals(ApiResult.Error(AuthError.VERIFICATION_CODE_NOT_FOUND), result)

        verify { TestConstants.userSource.findUserForVerification(TestConstants.TEST_EMAIL, SendCodeType.REGISTRATION) }
    }

    @Test
    fun `verifyEmailByCode returns EXPIRED_CODE when code is incorrect`() = runBlocking {
        every { TestConstants.userSource.findUserForVerification(TestConstants.TEST_EMAIL, SendCodeType.REGISTRATION) } returns UserVerify(
            id = 1,
            email = TestConstants.TEST_EMAIL,
            passwordHash = null,
            verificationHashedCode = TestConstants.HASHED_VERIFICATION_CODE,
            createdAt = null
        )
        every { TestConstants.passwordHasher.verifyPassword(TestConstants.VERIFICATION_CODE, TestConstants.HASHED_VERIFICATION_CODE) } returns false

        val result = userService.verifyEmailByCode(TestConstants.TEST_EMAIL, TestConstants.VERIFICATION_CODE)
        assertEquals(ApiResult.Error(AuthError.EXPIRED_CODE), result)

        verify {
            TestConstants.userSource.findUserForVerification(TestConstants.TEST_EMAIL, SendCodeType.REGISTRATION)
            TestConstants.passwordHasher.verifyPassword(TestConstants.VERIFICATION_CODE, TestConstants.HASHED_VERIFICATION_CODE)
        }
    }

    @Test
    fun `verifyEmailByCode returns success when code is correct`() = runBlocking {
        every { TestConstants.userSource.findUserForVerification(TestConstants.TEST_EMAIL, SendCodeType.REGISTRATION) } returns UserVerify(
            id = 1,
            email = TestConstants.TEST_EMAIL,
            verificationHashedCode = TestConstants.HASHED_VERIFICATION_CODE,
            createdAt = null,
            passwordHash = null,
        )
        every { TestConstants.passwordHasher.verifyPassword(TestConstants.VERIFICATION_CODE, TestConstants.HASHED_VERIFICATION_CODE) } returns true
        every { TestConstants.userSource.verifyEmail(1) } just Runs

        val result = userService.verifyEmailByCode(TestConstants.TEST_EMAIL, TestConstants.VERIFICATION_CODE)
        assertEquals(ApiResult.Success("Email confirmed successfully!"), result)

        verifyOrder {
            TestConstants.userSource.findUserForVerification(TestConstants.TEST_EMAIL, SendCodeType.REGISTRATION)
            TestConstants.passwordHasher.verifyPassword(TestConstants.VERIFICATION_CODE, TestConstants.HASHED_VERIFICATION_CODE)
            TestConstants.userSource.verifyEmail(1)
        }
    }
}
