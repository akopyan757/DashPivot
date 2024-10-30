package com.cheesecake.auth.feature.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.cheesecake.auth.feature.common.EmailTextField

@Preview
@Composable
fun PreviewEmailTextField() {
    var email by remember { mutableStateOf("") }

    EmailTextField(
        email = email,
        onEmailChange = { email = it },
        label = "Email"
    )
}