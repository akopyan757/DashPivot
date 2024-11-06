package com.cheesecake.auth.feature.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecake.auth.feature.domain.usecase.VerificationUseCase
import com.cheesecake.auth.feature.state.RESEND_KEY
import com.cheesecake.auth.feature.state.VerificationResendTimer
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.ui.navigator.state.IStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VerificationViewModel(
    private val verificationUseCase: VerificationUseCase,
    private val stateManager: IStateManager,
) : ViewModel() {

    private val _verificationState = MutableStateFlow(VerificationState())
    val verificationState: StateFlow<VerificationState> get() = _verificationState

    fun verifyToken(email: String, code: String) {
        _verificationState.value = _verificationState.value.copy(
            logicState = VerificationLogicState.Loading
        )
        viewModelScope.launch {
            verificationUseCase(email, code).collect { result ->
                _verificationState.value = when (result) {
                    is ApiResult.Success<*> -> _verificationState.value.copy(
                        logicState = VerificationLogicState.Success
                    )
                    is ApiResult.Error<*> ->_verificationState.value.copy(
                        logicState = VerificationLogicState.Error(result.error.message)
                    )
                    else -> _verificationState.value.copy(
                        logicState = VerificationLogicState.Idle
                    )
                }
            }
        }
    }

    fun resetToIdle() {
        _verificationState.value = _verificationState.value.copy(
            logicState = VerificationLogicState.Idle
        )
    }

    fun resetToIdleWithTimer(email: String) {
        val resendTimer = stateManager.getSerializableState(RESEND_KEY, VerificationResendTimer::class)
        if (resendTimer?.email == email && !resendTimer.canResend()) {
            val restSeconds = resendTimer.calculateRestSeconds()
            _verificationState.value = _verificationState.value.copy(
                formData = VerificationFormData(restTime = restSeconds),
            )
        }
    }
}

data class VerificationState(
    val formData: VerificationFormData = VerificationFormData(),
    val logicState: VerificationLogicState = VerificationLogicState.Idle,
)

data class VerificationFormData(
    val restTime: Int = -1,
)

sealed class VerificationLogicState {
    data object Idle : VerificationLogicState()
    data object Loading : VerificationLogicState()
    data object Success : VerificationLogicState()
    data class Error(val message: String) : VerificationLogicState()
}