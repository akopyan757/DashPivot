package com.cheesecake.auth.feature.registration

import androidx.compose.runtime.Composable
import com.cheesecake.auth.feature.login.LoginNavigate
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


internal class MyKoinComponent : KoinComponent {
    fun getViewModel(): SignUpViewModel = get()
}

@Composable
actual fun SignUpScreen(signUpNavigate: SignUpNavigate) {
    SignUpScreen(MyKoinComponent().getViewModel(), signUpNavigate)
}