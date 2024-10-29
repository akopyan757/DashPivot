package com.cheesecake.auth.feature.verification

import androidx.compose.runtime.Composable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class MyKoinComponent : KoinComponent {
    fun getViewModel(): VerificationViewModel = get()
}

@Composable
actual fun VerificationScreen(token: String, onDismiss: () -> Unit) {
    VerificationScreen(
        viewModel = MyKoinComponent().getViewModel(),
        token = token,
        onDismiss = onDismiss
    )
}