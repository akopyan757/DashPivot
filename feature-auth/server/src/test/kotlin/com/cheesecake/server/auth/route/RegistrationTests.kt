package com.cheesecake.server.auth.route

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.config.Config
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.service.UserService
import com.cheesecake.server.auth.route.common.TestConstants
import com.cheesecake.server.auth.route.repository.UserRepository
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class RegistrationTests {

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

        clearMocks(TestConstants.passwordHasher)
    }

    @Test
    fun `successful registration`() = runTest {
        val registerRequest = RegisterRequest(email = TestConstants.TEST_EMAIL, password = TestConstants.TEST_PASSWORD)

        TestConstants.clearMocks()
        every { TestConstants.passwordHasher.hashPassword(registerRequest.password) } returns TestConstants.HASHED_PASSWORD
        every { TestConstants.passwordHasher.hashPassword(TestConstants.VERIFICATION_CODE) } returns TestConstants.HASHED_VERIFICATION_CODE
        every { TestConstants.verifyCodeGenerator.generateVerificationCode(Config.VERIFICATION_CODE_COUNT) } returns TestConstants.VERIFICATION_CODE
        every { TestConstants.userSource.canSendVerificationCode(registerRequest.email) } returns true
        every { TestConstants.emailService.sendVerificationEmail(registerRequest.email, TestConstants.VERIFICATION_CODE) } returns true

        val result = userService.registerUser(registerRequest)

        assertEquals(ApiResult.Success(TestConstants.SUCCESS_MESSAGE), result)

        verify(exactly = 1) { TestConstants.passwordHasher.hashPassword(registerRequest.password) }
        verify(exactly = 1) { TestConstants.verifyCodeGenerator.generateVerificationCode(Config.VERIFICATION_CODE_COUNT) }
        verify(exactly = 1) { TestConstants.passwordHasher.hashPassword(TestConstants.VERIFICATION_CODE) }

        coVerify(exactly = 1) {
            TestConstants.emailService.sendVerificationEmail(
                registerRequest.email,
                TestConstants.VERIFICATION_CODE
            )
        }
    }

    @Test
    fun `email already taken error`() = runTest {
        val registerRequest = RegisterRequest(email = TestConstants.TEST_EMAIL, password = TestConstants.TEST_PASSWORD)

        TestConstants.clearMocks()
        every { TestConstants.userSource.isEmailTakenAndVerified(registerRequest.email) } returns true

        val result = userService.registerUser(registerRequest)

        assertEquals(ApiResult.Error(RegisterError.EMAIL_TAKEN), result)
        verify(exactly = 1) { TestConstants.userSource.isEmailTakenAndVerified(registerRequest.email) }
        confirmVerified(TestConstants.passwordHasher, TestConstants.verifyCodeGenerator, TestConstants.emailService)
    }

    @Test
    fun `invalid email format error`() = runTest {
        val registerRequest = RegisterRequest(email = TestConstants.INVALID_EMAIL, password = TestConstants.TEST_PASSWORD)

        TestConstants.clearMocks()
        every { TestConstants.userSource.isEmailTakenAndVerified(registerRequest.email) } returns false

        val result = userService.registerUser(registerRequest)

        assertEquals(ApiResult.Error(RegisterError.INVALID_EMAIL_FORMAT), result)
        confirmVerified(TestConstants.passwordHasher, TestConstants.verifyCodeGenerator, TestConstants.emailService)
    }

    @Test
    fun `invalid password format error`() = runTest {
        val registerRequest = RegisterRequest(email = TestConstants.TEST_EMAIL, password = TestConstants.SHORT_PASSWORD)

        TestConstants.clearMocks()
        val result = userService.registerUser(registerRequest)

        assertEquals(ApiResult.Error(RegisterError.INVALID_PASSWORD), result)
        confirmVerified(TestConstants.verifyCodeGenerator, TestConstants.emailService)
    }

    @Test
    fun `too many requests error`() = runTest {
        val registerRequest = RegisterRequest(email = TestConstants.TEST_EMAIL, password = TestConstants.TEST_PASSWORD)

        TestConstants.clearMocks()
        every { TestConstants.userSource.canSendVerificationCode(registerRequest.email) } returns false

        val result = userService.registerUser(registerRequest)

        assertEquals(ApiResult.Error(RegisterError.TOO_MANY_REQUESTS), result)
        verify(exactly = 1) { TestConstants.userSource.canSendVerificationCode(registerRequest.email) }
        confirmVerified(TestConstants.passwordHasher, TestConstants.verifyCodeGenerator, TestConstants.emailService)
    }

    @Test
    fun `verification letter sending error`() = runTest {
        val registerRequest = RegisterRequest(email = TestConstants.TEST_EMAIL, password = TestConstants.TEST_PASSWORD)

        every { TestConstants.passwordHasher.hashPassword(registerRequest.password) } returns TestConstants.HASHED_PASSWORD
        every { TestConstants.verifyCodeGenerator.generateVerificationCode(Config.VERIFICATION_CODE_COUNT) } returns TestConstants.VERIFICATION_CODE
        every { TestConstants.userSource.canSendVerificationCode(registerRequest.email) } returns true
        every { TestConstants.passwordHasher.hashPassword(TestConstants.VERIFICATION_CODE) } returns TestConstants.HASHED_VERIFICATION_CODE
        every { TestConstants.emailService.sendVerificationEmail(registerRequest.email, TestConstants.VERIFICATION_CODE) } throws Exception("Sending error")
    }
}
