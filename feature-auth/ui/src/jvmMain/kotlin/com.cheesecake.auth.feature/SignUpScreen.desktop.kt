package com.cheesecake.auth.feature

import androidx.compose.runtime.Composable
import com.cheesecake.auth.feature.viewmodel.SignUpViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class MyKoinComponent : KoinComponent {
    fun getViewModel(): SignUpViewModel = get()
}

@Composable
actual fun SignUpScreen() {
    SignUpScreen(MyKoinComponent().getViewModel())
}