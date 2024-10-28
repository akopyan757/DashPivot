package com.cheesecake.auth.feature

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
actual fun SignUpScreen() {
    com.cheesecake.auth.feature.ui.SignUpScreen(viewModel = koinViewModel())
}