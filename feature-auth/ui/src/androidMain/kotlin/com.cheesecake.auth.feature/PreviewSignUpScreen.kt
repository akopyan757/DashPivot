package com.cheesecake.auth.feature

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cheesecake.auth.feature.di.AndroidKoinComponent
import com.cheesecake.auth.feature.registration.SignUpNavigate
import com.cheesecake.auth.feature.registration.SignUpScreen

@Preview
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen(
        AndroidKoinComponent.getSignUpViewModel(),
        object : SignUpNavigate {}
    )
}