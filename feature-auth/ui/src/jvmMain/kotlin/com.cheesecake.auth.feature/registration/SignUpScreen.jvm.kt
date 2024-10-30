package com.cheesecake.auth.feature.registration

import androidx.compose.runtime.Composable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


internal class MyKoinComponent : KoinComponent {
    fun getViewModel(): SignUpViewModel = get()
}

@Composable
actual fun SignUpScreen() {
    SignUpScreen(MyKoinComponent().getViewModel())
}