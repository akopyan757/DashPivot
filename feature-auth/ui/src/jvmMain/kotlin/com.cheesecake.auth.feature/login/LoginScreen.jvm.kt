package com.cheesecake.auth.feature.login

import androidx.compose.runtime.Composable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


internal class MyKoinComponent : KoinComponent {
    fun getViewModel(): LoginViewModel = get()
}

@Composable
actual fun LoginScreen(loginNavigate: LoginNavigate) {
    LoginScreen(MyKoinComponent().getViewModel(), loginNavigate)
}