package com.cheesecake.auth.feature.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecake.auth.data.repository.IUserRepository
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.model.RegisterError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val userRepository: IUserRepository
): ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> get() = _signUpState

    fun signUp(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading

            userRepository.registerUser(email, password).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _signUpState.value = SignUpState.Success(result.data)
                    }
                    is ApiResult.Error -> {
                        println("result: ${result.error}")
                        _signUpState.value = SignUpState.Error(result.error)
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
    data class Error(val error: RegisterError) : SignUpState()
}