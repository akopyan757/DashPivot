package com.cheesecake.auth.feature.verification

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
actual fun VerificationScreen(token: String, onDismiss: () -> Unit) {
    VerificationScreen(viewModel = koinViewModel(), token = token, onDismiss = onDismiss)
}