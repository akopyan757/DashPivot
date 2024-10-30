package com.cheesecake.auth.feature.registration

import androidx.compose.runtime.Composable
import com.cheesecake.auth.feature.login.LoginNavigate
import org.koin.androidx.compose.koinViewModel

@Composable
actual fun SignUpScreen(signUpNavigate: SignUpNavigate) {
    SignUpScreen(viewModel = koinViewModel(), signUpNavigate=signUpNavigate)
}