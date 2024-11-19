package com.cheesecake.auth.feature.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecake.auth.feature.domain.usecase.RegisterUseCase
import com.cheesecake.auth.feature.state.RESEND_KEY
import com.cheesecake.auth.feature.state.VerificationResendTimer
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.utils.formatPasswordErrors
import com.cheesecake.common.auth.utils.isValidEmail
import com.cheesecake.common.auth.utils.isValidPassword
import com.cheesecake.common.auth.utils.validatePassword
import com.cheesecake.common.ui.state.UIState
import com.cheesecake.common.ui.state.UIStateManager
import com.cheesecake.common.ui.state.cache.StateCache
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class SignUpViewModel(
    private val registerUseCase: RegisterUseCase,
    private val stateCache: StateCache,
    private val stateManager: UIStateManager<SignUpState>,
): ViewModel() {

    val signUpUiState: StateFlow<SignUpState> = stateManager.state

    fun onResetSuccess() {
        stateManager.update {
            if (logicState is SignUpLogicState.Success) {
                copy(logicState = SignUpLogicState.Idle)
            } else {
                this
            }
        }
    }

    fun updateEmail(email: String) {
        stateManager.update {
            val errorState = logicState as? SignUpLogicState.Error
            copy(
                formData = formData.copy(email = email),
                logicState = errorState?.copy(emailErrorMessage = null) ?: logicState
            )
        }
    }

    fun updatePassword(password: String) {
        stateManager.update {
            val errorState = logicState as? SignUpLogicState.Error
            copy(
                formData = formData.copy(password = password),
                logicState = errorState?.copy(passwordMessage = null) ?: logicState
            )
        }
    }

    fun updateConfirmationPassword(confirmPassword: String) {
        stateManager.update {
            val errorState = logicState as? SignUpLogicState.Error
            copy(
                formData = formData.copy(confirmPassword = confirmPassword),
                logicState = errorState?.copy(confirmPasswordMessage = null) ?: logicState
            )
        }
    }

    fun updatePasswordVisible(isPasswordVisible: Boolean) {
        stateManager.update {
            copy(formData = formData.copy(passwordVisible = isPasswordVisible))
        }
    }

    fun updateConfirmPasswordVisible(isConfirmPasswordVisible: Boolean) {
        stateManager.update {
            copy(formData = formData.copy(confirmPasswordVisible = isConfirmPasswordVisible))
        }
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
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
            stateManager.update { copy(logicState = errorState) }
        } else {
            stateManager.update { copy(logicState = SignUpLogicState.Loading) }
            viewModelScope.launch {
                registerUseCase(email, password).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            updateResendTimer(email)
                            stateManager.update {
                                copy(logicState = SignUpLogicState.Success(result.data))
                            }
                        }

                        is ApiResult.Error -> {
                            stateManager.update {
                                copy(logicState = SignUpLogicState.Error(result.error))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateResendTimer(email: String) {
        val state = VerificationResendTimer.init(email)
        stateCache.setSerializableState(RESEND_KEY, state, VerificationResendTimer.serializer())
    }
}

@Serializable
data class SignUpState(
    val formData: SignUpFormData = SignUpFormData(),
    val logicState: SignUpLogicState = SignUpLogicState.Idle
): UIState {
    companion object {
        val KEY = SignUpState::class.simpleName.orEmpty()
    }
}

@Serializable
data class SignUpFormData(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val confirmPassword: String = "",
    val confirmPasswordVisible: Boolean = false,
)

@Serializable
sealed class SignUpLogicState {
    @Serializable data object Idle : SignUpLogicState()
    @Serializable data object Loading : SignUpLogicState()
    @Serializable data class Success(val message: String) : SignUpLogicState()
    @Serializable data class Error(
        val error: RegisterError? = null,
        val emailErrorMessage: String? = null,
        val passwordMessage: String? = null,
        val confirmPasswordMessage: String? = null,
    ) : SignUpLogicState()
}