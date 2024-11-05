package com.cheesecake.auth.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.cheesecake.auth.feature.di.AppKoinComponent
import com.cheesecake.auth.feature.login.LoginNavigate
import com.cheesecake.auth.feature.login.LoginScreen
import com.cheesecake.auth.feature.registration.SignUpNavigate
import com.cheesecake.auth.feature.registration.SignUpScreen
import com.cheesecake.auth.feature.verification.VerificationScreen
import com.cheesecake.common.ui.navigator.Navigator
import com.cheesecake.common.ui.navigator.Screen

@Composable
fun getComposable(
    screen: Screen,
    navigator: Navigator,
    appKoinComponent: AppKoinComponent,
) {
    when (screen) {
        is AuthScreen.Login -> {
            LoginScreen(appKoinComponent, object : LoginNavigate {
                override fun toRegistration() {
                    navigator.navigateTo(AuthScreen.Registration)
                }
            })
        }
        is AuthScreen.Registration -> {
            SignUpScreen(appKoinComponent, object : SignUpNavigate {
                override fun toLogin() {
                    navigator.navigateTo(AuthScreen.Login)
                }
                override fun toVerification(email: String) {
                    navigator.navigateTo(AuthScreen.Verification(email))
                }
            })
        }
        is AuthScreen.Verification -> {
            VerificationScreen(appKoinComponent, screen.email) {
                navigator.goBack(AuthScreen.Login)
            }
        }
        else -> {}
    }
}