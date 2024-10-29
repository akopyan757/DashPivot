package com.cheesecake.dashpivot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import com.cheesecake.auth.feature.ui.SignUpScreen
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.cheesecake.auth.feature.verification.VerificationScreen
import com.cheesecake.dashpivot.entry.AppEntry
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(appEntry: AppEntry = AppEntry()) {
    var verificationVisibility by remember { mutableStateOf(appEntry.token != null) }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().padding(WindowInsets.ime.asPaddingValues())) {
            SignUpScreen()

            if (verificationVisibility && appEntry.token != null) {
                VerificationScreen(
                    token = appEntry.token,
                    onDismiss = { verificationVisibility = false }
                )
            }
        }
    }
}