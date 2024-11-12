package com.cheesecake.server.auth.route

import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.config.Config
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.service.UserService
import com.cheesecake.server.auth.route.common.TestConstants
import com.cheesecake.server.auth.route.common.TestDatabase
import com.cheesecake.server.auth.route.database.UserSource
import com.cheesecake.server.auth.route.database.Users
import com.cheesecake.server.auth.route.database.VerificationCodes
import com.cheesecake.server.auth.route.repository.UserRepository
import io.mockk.every
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertReturning
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class RegistrationIntegrationTests {

    private lateinit var userService: UserService
    private lateinit var userSource: UserSource // Используем реальный UserSource

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
    }

    @AfterTest
    fun tearDown() {
        TestDatabase.dropTestDatabase()
    }

    @Test
    fun `successful registration with real database`() = runTest {
        val registerRequest = RegisterRequest(
            email = TestConstants.TEST_EMAIL,
            password = TestConstants.TEST_PASSWORD
        )

        every { TestConstants.passwordHasher.hashPassword(registerRequest.password) } returns TestConstants.HASHED_PASSWORD
        every { TestConstants.passwordHasher.hashPassword(TestConstants.VERIFICATION_CODE) } returns TestConstants.HASHED_VERIFICATION_CODE
        every { TestConstants.verifyCodeGenerator.generateVerificationCode(Config.VERIFICATION_CODE_COUNT) } returns TestConstants.VERIFICATION_CODE
        every { TestConstants.emailService.sendVerificationEmail(registerRequest.email, TestConstants.VERIFICATION_CODE) } returns true

        val result = userService.registerUser(registerRequest)

        assertTrue(result is ApiResult.Success)
        assertEquals("User registered successfully", result.data)

        transaction {
            val user = userSource.findUserByEmail(registerRequest.email)
            assertNotNull(user)
            assertEquals(registerRequest.email, user.email)
            assertEquals(TestConstants.HASHED_PASSWORD, user.passwordHash)
            assertFalse(user.isVerified)
        }
    }

    @Test
    fun `successful registration when delay time has passed since last verification code`() = runTest {
        val testEmail = TestConstants.TEST_EMAIL
        val registerRequest = RegisterRequest(email = testEmail, password = "Zaq12345@")
        val delaySeconds = Config.VERIFICATION_CODE_SENDING_DELAY_SEC + 1 // на 1 секунду больше нужного интервала
        val previousTime = LocalDateTime.now().minusSeconds(delaySeconds)

        every { TestConstants.emailService.sendVerificationEmail(registerRequest.email, any()) } returns true

        transaction {
            val id = Users.insertReturning(listOf(Users.id)) {
                it[email] = registerRequest.email
                it[passwordHash] = TestConstants.HASHED_PASSWORD
                it[isVerified] = false
            }.map { it[Users.id] }.singleOrNull()

            VerificationCodes.insert {
                it[userId] = id ?: 0
                it[code] = "oldVerificationCode"
                it[createdAt] = previousTime
            }
        }

        val result = userService.registerUser(registerRequest)

        assertEquals(result, ApiResult.Success("User registered successfully"))
    }

    @Test
    fun `registration with email already taken in real database`() = runTest {
        // Arrange: добавляем пользователя в базу перед регистрацией
        transaction {
            userSource.createUser(
                email = TestConstants.TEST_EMAIL,
                passwordHash = TestConstants.HASHED_PASSWORD,
                isVerified = true,
                verificationToken = "hashedVerificationCode",
            )
        }

        val registerRequest = RegisterRequest(email = TestConstants.TEST_EMAIL, password = "Zaq12345@")
        val result = userService.registerUser(registerRequest)

        assertTrue(result is ApiResult.Error)
        assertEquals(RegisterError.EMAIL_TAKEN, result.error)
    }


    @Test
    fun `too many requests error when last code sent less than a minute ago`() = runTest {
        // Arrange: добавляем пользователя с недавней отправкой кода подтверждения
        val email = TestConstants.TEST_EMAIL
        val seconds = Config.VERIFICATION_CODE_SENDING_DELAY_SEC / 2 // прошло меньше времени, чем нужно
        val recentTime = LocalDateTime.now().minusSeconds(seconds)

        transaction {
            val id = Users.insertReturning(listOf(Users.id)) {
                it[Users.email] = email
                it[passwordHash] = TestConstants.HASHED_PASSWORD
                it[isVerified] = false
            }.map { it[Users.id] }.singleOrNull()
            VerificationCodes.insert {
                it[userId] = id ?: 0
                it[code] = "hashedVerificationCode"
                it[createdAt] = recentTime
            }
        }

        // Act: выполняем регистрацию, что должно привести к ошибке TOO_MANY_REQUESTS из-за недостаточного интервала времени
        val registerRequest = RegisterRequest(email = email, password = "Zaq12345@")
        val result = userService.registerUser(registerRequest)

        // Assert
        assertEquals(ApiResult.Error(RegisterError.TOO_MANY_REQUESTS), result)
    }


    @Test
    fun `create user error for existing email`() = runTest {
        // Arrange: создаем пользователя с email, который затем будет повторно использован
        val email = TestConstants.TEST_EMAIL
        val enoughTimeSeconds = Config.VERIFICATION_CODE_SENDING_DELAY_SEC * 2
        val recentTime = LocalDateTime.now().minusSeconds(enoughTimeSeconds)
        transaction {
            Users.insert {
                it[Users.email] = email
                it[passwordHash] = TestConstants.HASHED_PASSWORD
                it[isVerified] = true // Предполагается, что пользователь уже подтвержден
                it[verificationToken] = "hashedVerificationCode"
                it[createdAt] = recentTime
            }
        }

        val registerRequest = RegisterRequest(email = email, password = "Zaq12345@")
        val result = userService.registerUser(registerRequest)

        // Assert
        assertEquals(ApiResult.Error(RegisterError.EMAIL_TAKEN), result)
    }
}
