package com.cheesecake.auth.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecake.auth.data.repository.IUserRepository
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.utils.formatPasswordErrors
import com.cheesecake.common.auth.utils.isValidEmail
import com.cheesecake.common.auth.utils.isValidPassword
import com.cheesecake.common.auth.utils.validatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: IUserRepository
): ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    fun onResetEmailError() {
        if (_loginState.value as? LoginState.Error != null) {
            val errorState = _loginState.value as LoginState.Error
            _loginState.value = errorState.copy(emailErrorMessage = null)
        }
    }

    fun onResetPasswordError() {
        if (_loginState.value as? LoginState.Error != null) {
            val errorState = _loginState.value as LoginState.Error
            _loginState.value = errorState.copy(passwordMessage = null)
        }
    }

    fun login(email: String, password: String) {
        var errorState: LoginState.Error = LoginState.Error()

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

        val hasError = errorState.emailErrorMessage != null ||
            errorState.passwordMessage != null

        if (hasError) {
            _loginState.value = errorState
        } else {
            _loginState.value = LoginState.Loading

            viewModelScope.launch {
                userRepository.loginUser(email, password).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _loginState.value = LoginState.Success(result.data)
                        }

                        is ApiResult.Error -> {
                            _loginState.value = LoginState.Error(result.error)
                        }
                    }
                }
            }
        }
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(
        val error: LoginError? = null,
        val emailErrorMessage: String? = null,
        val passwordMessage: String? = null,
    ) : LoginState()
}