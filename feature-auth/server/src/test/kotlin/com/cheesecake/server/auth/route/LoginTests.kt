package com.cheesecake.server.auth.route

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.User
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.service.UserService
import com.cheesecake.server.auth.route.common.TestConstants
import com.cheesecake.server.auth.route.repository.UserRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class LoginTests {

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
    fun `successful login`() = runTest {
        val loginRequest = LoginRequest(email = TestConstants.TEST_EMAIL, password = TestConstants.TEST_PASSWORD)
        val user = User(id = TestConstants.TEST_USER_ID, email = TestConstants.TEST_EMAIL, passwordHash = TestConstants.HASHED_PASSWORD, isVerified = true)

        TestConstants.clearMocks()
        every { TestConstants.userSource.findUserByEmail(loginRequest.email) } returns user
        every { TestConstants.passwordHasher.verifyPassword(loginRequest.password, TestConstants.HASHED_PASSWORD) } returns true
        every { TestConstants.tokenGenerator.generateToken(TestConstants.TEST_USER_ID.toString()) } returns TestConstants.TOKEN

        val result = userService.loginUser(loginRequest)

        assertEquals(ApiResult.Success(TestConstants.TOKEN), result)

        verify(exactly = 1) { TestConstants.userSource.findUserByEmail(loginRequest.email) }
        verify(exactly = 1) { TestConstants.passwordHasher.verifyPassword(loginRequest.password, TestConstants.HASHED_PASSWORD) }
        verify(exactly = 1) { TestConstants.tokenGenerator.generateToken(TestConstants.TEST_USER_ID.toString()) }
        confirmVerified(TestConstants.emailService, TestConstants.verifyCodeGenerator)
    }

    @Test
    fun `user not found error`() = runTest {
        val loginRequest = LoginRequest(email = TestConstants.TEST_EMAIL, password = TestConstants.TEST_PASSWORD)
        TestConstants.clearMocks()
        every { TestConstants.userSource.findUserByEmail(loginRequest.email) } returns null

        val result = userService.loginUser(loginRequest)

        assertEquals(ApiResult.Error(LoginError.USER_NOT_FOUND), result)
        verify(exactly = 1) { TestConstants.userSource.findUserByEmail(loginRequest.email) }
        confirmVerified(TestConstants.passwordHasher, TestConstants.tokenGenerator, TestConstants.emailService, TestConstants.verifyCodeGenerator)
    }

    @Test
    fun `invalid password error`() = runTest {
        val loginRequest = LoginRequest(email = TestConstants.TEST_EMAIL, password = TestConstants.TEST_PASSWORD)

        TestConstants.clearMocks()
        every { TestConstants.userSource.findUserByEmail(loginRequest.email) } returns TestConstants.USER_VERIFIED
        every { TestConstants.passwordHasher.verifyPassword(loginRequest.password, TestConstants.HASHED_PASSWORD) } returns false

        val result = userService.loginUser(loginRequest)

        assertEquals(ApiResult.Error(LoginError.INVALID_PASSWORD), result)
        verify(exactly = 1) { TestConstants.userSource.findUserByEmail(loginRequest.email) }
        verify(exactly = 1) { TestConstants.passwordHasher.verifyPassword(loginRequest.password, TestConstants.HASHED_PASSWORD) }
        confirmVerified(TestConstants.tokenGenerator, TestConstants.emailService, TestConstants.verifyCodeGenerator)
    }

    @Test
    fun `email not verified error`() = runTest {
        val loginRequest = LoginRequest(email = TestConstants.TEST_EMAIL, password = TestConstants.TEST_PASSWORD)
        TestConstants.clearMocks()
        every { TestConstants.userSource.findUserByEmail(loginRequest.email) } returns TestConstants.USER_NOT_VERIFIED
        every { TestConstants.passwordHasher.verifyPassword(loginRequest.password, TestConstants.HASHED_PASSWORD) } returns true

        val result = userService.loginUser(loginRequest)

        assertEquals(ApiResult.Error(LoginError.EMAIL_NOT_VERIFIED), result)
        verify(exactly = 1) { TestConstants.userSource.findUserByEmail(loginRequest.email) }
        verify(exactly = 1) { TestConstants.passwordHasher.verifyPassword(loginRequest.password, TestConstants.HASHED_PASSWORD) }
        confirmVerified(TestConstants.tokenGenerator, TestConstants.emailService, TestConstants.verifyCodeGenerator)
    }
}