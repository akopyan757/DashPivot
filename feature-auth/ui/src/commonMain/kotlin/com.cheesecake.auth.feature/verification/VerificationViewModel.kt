package com.cheesecake.auth.feature.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecake.auth.feature.domain.usecase.ResendCodeUseCase
import com.cheesecake.auth.feature.domain.usecase.VerificationUseCase
import com.cheesecake.auth.feature.login.LoginState
import com.cheesecake.auth.feature.registration.SignUpState
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.config.Config
import com.cheesecake.common.ui.state.UIState
import com.cheesecake.common.ui.state.UIStateManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class VerificationViewModel(
    private val verificationUseCase: VerificationUseCase,
    private val resendCodeUseCase: ResendCodeUseCase,
    private val stateManager: UIStateManager<VerificationState>,
    private val loginStateManager: UIStateManager<LoginState>,
    private val signUpState: UIStateManager<SignUpState>,
) : ViewModel() {

    val verificationState: StateFlow<VerificationState> get() = stateManager.state

    fun verifyToken(email: String, code: String) {
        stateManager.update { copy(logicState = VerificationLogicState.Loading) }
        viewModelScope.launch {
            verificationUseCase(email, code).collect { result ->
                stateManager.update {
                    when (result) {
                        is ApiResult.Success<*> -> {
                            loginStateManager.clear()
                            signUpState.clear()
                            copy(logicState = VerificationLogicState.Success)
                        }
                        is ApiResult.Error<*> -> copy(
                            logicState = VerificationLogicState.Error(result.error.message)
                        )
                        else -> copy(logicState = VerificationLogicState.Idle)
                    }
                }
            }
        }
    }

    fun resendCode(email: String) {
        stateManager.update { copy(formData = VerificationFormData(isResendLoading = true)) }
        viewModelScope.launch {
            resendCodeUseCase(email).collect { result ->
                when (result) {
                    is ApiResult.Success<String> -> {
                        resetTimer()
                    }

                    is ApiResult.Error -> {

                    }
                }
            }
        }
    }

    fun resetToIdle() {
        stateManager.update { copy(logicState = VerificationLogicState.Idle) }
    }

    fun resetTimer() {
        stateManager.update {
            val formData = VerificationFormData(
                restTime = Config.VERIFICATION_CODE_SENDING_DELAY_SEC.toInt(),
                isResendLoading = false
            )
            copy(formData = formData)
        }
    }
}

@Serializable
data class VerificationState(
    val formData: VerificationFormData = VerificationFormData(),
    val logicState: VerificationLogicState = VerificationLogicState.Idle,
): UIState {
    companion object {
        val KEY: String = VerificationState::class.simpleName.orEmpty()
    }
}

@Serializable
data class VerificationFormData(
    val restTime: Int = -1,
    val isResendLoading: Boolean = false,
)

@Serializable
sealed class VerificationLogicState {
    data object Idle : VerificationLogicState()
    data object Loading : VerificationLogicState()
    data object Success : VerificationLogicState()
    data class Error(val message: String) : VerificationLogicState()
}
