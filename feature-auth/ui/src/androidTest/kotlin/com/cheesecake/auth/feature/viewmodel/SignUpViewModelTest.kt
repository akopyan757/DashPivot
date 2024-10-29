package com.cheesecake.auth.feature.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cheesecake.auth.data.repository.UserRepository
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.RegisterError
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignUpViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SignUpViewModel
    private val userRepository: UserRepository = mockk()

    @Before
    fun setUp() {
        viewModel = SignUpViewModel(userRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun signUp_withValidEmailAndPassword_triggersLoadingAndSuccess(): Unit = runTest {
        // Setup
        val email = "test@example.com"
        val password = "Valid@Password1"
        val confirmPassword = "Valid@Password1"
        val expectedData = "Registered"

        // Mock successful registration
        coEvery { userRepository.registerUser(email, password) } returns flowOf(ApiResult.Success(expectedData))

        viewModel.signUp(email, password, confirmPassword)

        println("State: " + viewModel.signUpState.value)
        assertEquals(SignUpState.Loading, viewModel.signUpState.value)

        withTimeout(1000) {
            while (viewModel.signUpState.value != SignUpState.Success(expectedData)) {
                advanceUntilIdle()
            }
        }
        println("State: " + viewModel.signUpState.value)
        assertEquals(SignUpState.Success(expectedData), viewModel.signUpState.value)
    }

    @Test
    fun signUp_withEmptyEmail_triggersEmailError() {
        val email = ""
        val password = "Valid@Password1"
        val confirmPassword = password

        viewModel.signUp(email, password, confirmPassword)

        val state = viewModel.signUpState.value
        assertTrue(
            state is SignUpState.Error &&
            state.emailErrorMessage == RegisterError.EMPTY_EMAIL_ERROR.message
        )
    }

    @Test
    fun signUp_withEmptyPassword_triggersPasswordError() {
        val email = "test@example.com"
        val password = ""
        val confirmPassword = ""

        viewModel.signUp(email, password, confirmPassword)

        val state = viewModel.signUpState.value
        assertTrue(state is SignUpState.Error && state.passwordMessage == RegisterError.EMPTY_PASSWORD_ERROR.message)
    }

    @Test
    fun signUp_withNonMatchingPasswords_triggersConfirmationPasswordError() {
        val email = "test@example.com"
        val password = "Valid@Password1"
        val confirmPassword = "DifferentPassword2"

        // Act
        viewModel.signUp(email, password, confirmPassword)

        // Assert
        val state = viewModel.signUpState.value
        assertTrue(state is SignUpState.Error && state.confirmPasswordMessage == RegisterError.PASSWORD_MATCH.message)
    }
}
