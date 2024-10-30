package com.cheesecake.dashpivot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import com.cheesecake.auth.feature.registration.SignUpScreen
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.cheesecake.auth.feature.login.LoginNavigate
import com.cheesecake.auth.feature.login.LoginScreen
import com.cheesecake.auth.feature.registration.SignUpNavigate
import com.cheesecake.auth.feature.verification.VerificationScreen
import com.cheesecake.dashpivot.entry.AppEntry
import com.cheesecake.dashpivot.entry.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(appEntry: AppEntry = AppEntry()) {
    var verificationVisibility by remember { mutableStateOf(appEntry.token != null) }
    var currentScreen: Screen by remember { mutableStateOf(Screen.Login) }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().padding(WindowInsets.ime.asPaddingValues())) {
            when (currentScreen) {
                Screen.Registration -> SignUpScreen(object : SignUpNavigate {
                    override fun toLogin() {
                        currentScreen = Screen.Login
                    }
                })

                Screen.Login -> LoginScreen(object : LoginNavigate {
                    override fun toRegistration() {
                        currentScreen = Screen.Registration
                    }
                })
            }

            if (verificationVisibility && appEntry.token != null) {
                VerificationScreen(
                    token = appEntry.token,
                    onDismiss = { verificationVisibility = false }
                )
            }
        }
    }
}