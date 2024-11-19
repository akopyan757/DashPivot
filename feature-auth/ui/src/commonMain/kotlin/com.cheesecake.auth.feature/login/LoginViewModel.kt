package com.cheesecake.auth.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecake.auth.feature.domain.usecase.LoginUseCase
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.utils.formatPasswordErrors
import com.cheesecake.common.auth.utils.isValidEmail
import com.cheesecake.common.auth.utils.isValidPassword
import com.cheesecake.common.auth.utils.validatePassword
import com.cheesecake.common.ui.state.UIState
import com.cheesecake.common.ui.state.UIStateManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val stateStrategy: UIStateManager<LoginState>,
): ViewModel() {

    val loginState: StateFlow<LoginState> get() = stateStrategy.state

    fun onEmailChanged(email: String) {
        stateStrategy.update {
            val error = logicState as? LoginLogicState.Error
            copy(
                formData = formData.copy(email = email),
                logicState = error?.copy(emailErrorMessage = null) ?: logicState
            )
        }
    }

    fun onPasswordChanged(password: String) {
        stateStrategy.update {
            val errorState = logicState as? LoginLogicState.Error
            copy(
                formData = formData.copy(password = password),
                logicState = errorState?.copy(passwordMessage = null) ?: logicState
            )
        }
    }

    fun changePasswordVisibleChanged(isPasswordVisible: Boolean) {
        stateStrategy.update {
            copy(formData = formData.copy(passwordVisible = isPasswordVisible))
        }
    }

    fun login(email: String, password: String) {
        var errorState: LoginLogicState.Error = LoginLogicState.Error()

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
            stateStrategy.update { copy(logicState = errorState) }
        } else {
            stateStrategy.update { copy(logicState = LoginLogicState.Loading) }
            viewModelScope.launch {
                loginUseCase(email, password).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            stateStrategy.update {
                                copy(
                                    formData = LoginFormData(), // Clear form data
                                    logicState = LoginLogicState.Success(result.data)
                                )
                            }
                        }

                        is ApiResult.Error -> {
                            stateStrategy.update {
                                copy(logicState = LoginLogicState.Error(result.error))
                            }
                        }
                    }
                }
            }
        }
    }
}



@Serializable
data class LoginState(
    val formData: LoginFormData = LoginFormData(),
    val logicState: LoginLogicState = LoginLogicState.Idle
): UIState {
    companion object {
        val KEY = LoginState::class.simpleName.orEmpty()
    }
}

@Serializable
data class LoginFormData(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
)

@Serializable
sealed class LoginLogicState {
    @Serializable data object Idle : LoginLogicState()
    @Serializable data object Loading : LoginLogicState()
    @Serializable data class Success(val message: String) : LoginLogicState()
    @Serializable data class Error(
        val error: LoginError? = null,
        val emailErrorMessage: String? = null,
        val passwordMessage: String? = null,
    ) : LoginLogicState()
}