package com.cheesecake.server.auth.route

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.service.UserService
import com.cheesecake.server.auth.route.common.TestConstants
import com.cheesecake.server.auth.route.common.TestDatabase
import com.cheesecake.server.auth.route.database.UserSource
import com.cheesecake.server.auth.route.repository.UserRepository
import io.mockk.every
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginIntegrationTests {

    private lateinit var userService: UserService
    private lateinit var userSource: UserSource

    @BeforeTest
    fun setup() {
        TestDatabase.setupTestDatabase()
        userSource = UserSource()
        userService = UserRepository(
            emailService = TestConstants.emailService,
            passwordHasher = TestConstants.passwordHasher,
            verifyCodeGenerator = TestConstants.verifyCodeGenerator,
            tokenGenerator = TestConstants.tokenGenerator,
            userSource = userSource
        )

        // Pre-populate database with a test user
        transaction {
            userSource.createUser(
                email = TestConstants.TEST_EMAIL,
                passwordHash = TestConstants.HASHED_PASSWORD,
                isVerified = true,
                hashedVerificationCode = "",
                createdDateTime = TestConstants.TEST_CREATED_AT
            )
        }
    }

    @AfterTest
    fun tearDown() {
        TestDatabase.dropTestDatabase()
    }

    @Test
    fun `successful login with real database`() = runTest {
        val loginRequest = LoginRequest(
            email = TestConstants.TEST_EMAIL,
            password = TestConstants.TEST_PASSWORD
        )

        every { TestConstants.passwordHasher.verifyPassword(loginRequest.password, TestConstants.HASHED_PASSWORD) } returns true
        every { TestConstants.tokenGenerator.generateToken(TestConstants.TEST_USER_ID.toString()) } returns TestConstants.TOKEN

        val result = userService.loginUser(loginRequest)

        assertTrue(result is ApiResult.Success)
        assertEquals(TestConstants.TOKEN, result.data)
    }

    @Test
    fun `user not found error with real database`() = runTest {
        val loginRequest = LoginRequest(
            email = "nonexistent@example.com",
            password = TestConstants.TEST_PASSWORD
        )

        val result = userService.loginUser(loginRequest)

        assertTrue(result is ApiResult.Error)
        assertEquals(LoginError.USER_NOT_FOUND, result.error)
    }

    @Test
    fun `invalid password error with real database`() = runTest {
        val loginRequest = LoginRequest(
            email = TestConstants.TEST_EMAIL,
            password = "wrongpassword"
        )

        every { TestConstants.passwordHasher.verifyPassword(loginRequest.password, TestConstants.HASHED_PASSWORD) } returns false

        val result = userService.loginUser(loginRequest)

        assertTrue(result is ApiResult.Error)
        assertEquals(LoginError.INVALID_PASSWORD, result.error)
    }

    @Test
    fun `email not verified error with real database`() = runTest {
        // Pre-populate database with an unverified user
        transaction {
            userSource.createUser(
                email = TestConstants.TEST_EMAIL_NOT_VERIFIED,
                passwordHash = TestConstants.HASHED_PASSWORD,
                isVerified = false,
                hashedVerificationCode = "",
                createdDateTime = TestConstants.TEST_CREATED_AT
            )
        }

        val loginRequest = LoginRequest(
            email =  TestConstants.TEST_EMAIL_NOT_VERIFIED,
            password = TestConstants.TEST_PASSWORD
        )

        every { TestConstants.passwordHasher.verifyPassword(loginRequest.password, TestConstants.HASHED_PASSWORD) } returns true

        val result = userService.loginUser(loginRequest)

        assertTrue(result is ApiResult.Error)
        assertEquals(LoginError.EMAIL_NOT_VERIFIED, result.error)
    }
}