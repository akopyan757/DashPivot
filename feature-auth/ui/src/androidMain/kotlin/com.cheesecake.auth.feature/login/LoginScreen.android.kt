package com.cheesecake.auth.feature.login

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
actual fun LoginScreen(loginNavigate: LoginNavigate) {
    LoginScreen(viewModel = koinViewModel(), loginNavigate)
}