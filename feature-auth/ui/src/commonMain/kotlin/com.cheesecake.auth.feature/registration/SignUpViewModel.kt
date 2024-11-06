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

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpUiState: StateFlow<SignUpState> get() = _signUpState

    fun onResetSuccess() {
        if (_signUpState.value.logicState is SignUpLogicState.Success) {
            _signUpState.value = _signUpState.value.copy(logicState = SignUpLogicState.Idle)
        }
    }

    fun onResetEmailError() {
        if (_signUpState.value.logicState is SignUpLogicState.Error) {
            val errorState = _signUpState.value.logicState as SignUpLogicState.Error
            _signUpState.value = _signUpState.value.copy(
                logicState = errorState.copy(emailErrorMessage = null)
            )
        }
    }

    fun onResetPasswordError() {
        if (_signUpState.value.logicState is SignUpLogicState.Error) {
            val errorState = _signUpState.value.logicState as SignUpLogicState.Error
            _signUpState.value = _signUpState.value.copy(
                logicState = errorState.copy(passwordMessage = null)
            )
        }
    }

    fun onResetConfirmationPasswordError() {
        if (_signUpState.value.logicState as? SignUpLogicState.Error != null) {
            val errorState = _signUpState.value.logicState as SignUpLogicState.Error
            _signUpState.value = _signUpState.value.copy(
                logicState = errorState.copy(confirmPasswordMessage = null)
            )
        }
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        _signUpState.value = _signUpState.value.copy(
            formData = SignUpFormData(email, password, confirmPassword)
        )
        println("SignUpViewModel: signUp: ${_signUpState.value.formData}")

        var errorState: SignUpLogicState.Error = SignUpLogicState.Error()

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
            _signUpState.value = _signUpState.value.copy(logicState = errorState)
        } else {
            _signUpState.value = _signUpState.value.copy(logicState = SignUpLogicState.Loading)
            viewModelScope.launch {
                registerUseCase(email, password).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _signUpState.value = _signUpState.value.copy(logicState =
                                SignUpLogicState.Success(result.data)
                            )
                        }

                        is ApiResult.Error -> {
                            _signUpState.value = _signUpState.value.copy(logicState =
                                SignUpLogicState.Error(result.error)
                            )
                        }
                    }
                }
            }
        }
    }

}

data class SignUpState(
    val formData: SignUpFormData = SignUpFormData(),
    val logicState: SignUpLogicState = SignUpLogicState.Idle
)

data class SignUpFormData(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

sealed class SignUpLogicState {
    data object Idle : SignUpLogicState()
    data object Loading : SignUpLogicState()
    data class Success(val message: String) : SignUpLogicState()
    data class Error(
        val error: RegisterError? = null,
        val emailErrorMessage: String? = null,
        val passwordMessage: String? = null,
        val confirmPasswordMessage: String? = null,
    ) : SignUpLogicState()
}