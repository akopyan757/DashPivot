package com.cheesecake.auth.feature.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecake.auth.data.repository.IUserRepository
import com.cheesecake.common.api.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VerificationViewModel(private val repository: IUserRepository) : ViewModel() {

    private val _verificationState = MutableStateFlow<VerificationState>(VerificationState.Idle)
    val verificationState: StateFlow<VerificationState> get() = _verificationState

    fun verifyToken(token: String) {
        _verificationState.value = VerificationState.Loading
        viewModelScope.launch {
            println("VerificationScreen: viewModel: start: $token")
            repository.verifyUserToken(token).collect { result ->
                _verificationState.value = when (result) {
                    is ApiResult.Success<*> -> VerificationState.Success
                    is ApiResult.Error<*> -> VerificationState.Error(result.error.message)
                    else -> VerificationState.Idle
                }
            }
        }
    }
}

sealed class VerificationState {
    data object Idle : VerificationState()
    data object Loading : VerificationState()
    data object Success : VerificationState()
    data class Error(val message: String) : VerificationState()
}