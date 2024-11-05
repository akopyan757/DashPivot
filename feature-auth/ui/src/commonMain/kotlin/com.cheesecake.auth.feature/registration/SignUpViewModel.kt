package com.cheesecake.auth.feature.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecake.auth.feature.domain.usecase.RegisterUseCase
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.utils.formatPasswordErrors
import com.cheesecake.common.auth.utils.isValidEmail
import com.cheesecake.common.auth.utils.isValidPassword
import com.cheesecake.common.auth.utils.validatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val registerUseCase: RegisterUseCase
): ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> get() = _signUpState

    fun onResetSuccess() {
        if (_signUpState.value is SignUpState.Success) {
            _signUpState.value = SignUpState.Idle
        }
    }

    fun onResetEmailError() {
        if (_signUpState.value as? SignUpState.Error != null) {
            val errorState = _signUpState.value as SignUpState.Error
            _signUpState.value = errorState.copy(emailErrorMessage = null)
        }
    }

    fun onResetPasswordError() {
        if (_signUpState.value as? SignUpState.Error != null) {
            val errorState = _signUpState.value as SignUpState.Error
            _signUpState.value = errorState.copy(passwordMessage = null)
        }
    }

    fun onResetConfirmationPasswordError() {
        if (_signUpState.value as? SignUpState.Error != null) {
            val errorState = _signUpState.value as SignUpState.Error
            _signUpState.value = errorState.copy(confirmPasswordMessage = null)
        }
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        var errorState: SignUpState.Error = SignUpState.Error()

        if (email.isBlank()) {
            errorState = errorState.copy(
                emailErrorMessage = RegisterError.EMPTY_EMAIL_ERROR.message
            )
        } else if (!isValidEmail(email)) {
            errorState = errorState.copy(
                emailErrorMessage = RegisterError.INVALID_EMAIL_FORMAT.message
            )
        }

        if (password.isBlank()) {
            errorState = errorState.copy(
                passwordMessage = RegisterError.EMPTY_PASSWORD_ERROR.message
            )
        } else if (!isValidPassword(password)) {
            val passwordErrors = validatePassword(password)
            errorState = if (passwordErrors.isNotEmpty()) {
                errorState.copy(passwordMessage = formatPasswordErrors(passwordErrors))
            } else {
                errorState.copy(passwordMessage = RegisterError.INVALID_PASSWORD.message)
            }
        }

        if (password != confirmPassword) {
            errorState = errorState.copy(
                confirmPasswordMessage = RegisterError.PASSWORD_MATCH.message
            )
        }

        val hasError = errorState.emailErrorMessage != null ||
            errorState.passwordMessage != null ||
            errorState.confirmPasswordMessage != null

        if (hasError) {
            _signUpState.value = errorState
        } else {
            _signUpState.value = SignUpState.Loading
            viewModelScope.launch {
                registerUseCase(email, password).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _signUpState.value = SignUpState.Success(result.data)
                        }

                        is ApiResult.Error -> {
                            _signUpState.value = SignUpState.Error(result.error)
                        }
                    }
                }
            }
        }
    }
}

sealed class SignUpState {
    data object Idle : SignUpState()
    data object Loading : SignUpState()
    data class Success(val message: String) : SignUpState()
    data class Error(
        val error: RegisterError? = null,
        val emailErrorMessage: String? = null,
        val passwordMessage: String? = null,
        val confirmPasswordMessage: String? = null,
    ) : SignUpState()
}