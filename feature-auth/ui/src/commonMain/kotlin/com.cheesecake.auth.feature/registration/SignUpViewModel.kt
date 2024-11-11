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
import com.cheesecake.common.ui.navigator.state.IStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class SignUpViewModel(
    private val registerUseCase: RegisterUseCase,
    private val stateManager: IStateManager,
): ViewModel() {

    private val _signUpState = MutableStateFlow(
        stateManager.getSerializableState(SignUpState.KEY, SignUpState.serializer())
            ?: SignUpState()
    )
    val signUpUiState: StateFlow<SignUpState> get() = _signUpState

    init {
        viewModelScope.launch {
            _signUpState.collect {
                stateManager.setSerializableState(SignUpState.KEY, it, SignUpState.serializer())
            }
        }
    }

    fun onResetSuccess() {
        if (_signUpState.value.logicState is SignUpLogicState.Success) {
            _signUpState.value = _signUpState.value.copy(logicState = SignUpLogicState.Idle)
        }
    }

    fun updateEmail(email: String) {
        val logicState = _signUpState.value.logicState
        val errorState = logicState as? SignUpLogicState.Error
        val newLogicState = errorState?.copy(passwordMessage = null) ?: logicState

        _signUpState.value = _signUpState.value.copy(
            formData = _signUpState.value.formData.copy(email = email),
            logicState = newLogicState
        )
    }

    fun updatePassword(password: String) {
        val logicState = _signUpState.value.logicState
        val errorState = logicState as? SignUpLogicState.Error
        val newLogicState = errorState?.copy(passwordMessage = null) ?: logicState

        _signUpState.value = _signUpState.value.copy(
            formData = _signUpState.value.formData.copy(password = password),
            logicState = newLogicState
        )
    }

    fun updateConfirmationPassword(confirmPassword: String) {
        val logicState = _signUpState.value.logicState
        val errorState = logicState as? SignUpLogicState.Error
        val newLogicState = errorState?.copy(confirmPasswordMessage = null) ?: logicState

        _signUpState.value = _signUpState.value.copy(
            formData = _signUpState.value.formData.copy(confirmPassword = confirmPassword),
            logicState = newLogicState
        )
    }

    fun updatePasswordVisible(isPasswordVisible: Boolean) {
        _signUpState.value = _signUpState.value.copy(
            formData = _signUpState.value.formData.copy(passwordVisible = isPasswordVisible)
        )
    }

    fun updateConfirmPasswordVisible(isConfirmPasswordVisible: Boolean) {
        _signUpState.value = _signUpState.value.copy(
            formData = _signUpState.value.formData.copy(
                confirmPasswordVisible = isConfirmPasswordVisible
            )
        )
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        _signUpState.value = _signUpState.value.copy(
            formData = _signUpState.value.formData.copy(
                email = email, password = password, confirmPassword = confirmPassword
            )
        )

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
                            updateResendTimer(email)
                            _signUpState.value = _signUpState.value.copy(
                                logicState = SignUpLogicState.Success(result.data)
                            )
                        }

                        is ApiResult.Error -> {
                            _signUpState.value = _signUpState.value.copy(
                                logicState = SignUpLogicState.Error(result.error)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateResendTimer(email: String) {
        val state = VerificationResendTimer.init(email)
        stateManager.setSerializableState(RESEND_KEY, state, VerificationResendTimer::class)
    }

}

@Serializable
data class SignUpState(
    val formData: SignUpFormData = SignUpFormData(),
    val logicState: SignUpLogicState = SignUpLogicState.Idle
) {
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