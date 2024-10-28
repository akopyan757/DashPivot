package com.cheesecake.auth.feature.ui

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
actual fun SignUpScreen() {
    SignUpScreen(viewModel = koinViewModel())
}