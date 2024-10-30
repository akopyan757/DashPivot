package com.cheesecake.auth.feature.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.cheesecake.auth.feature.common.PasswordTextField

@Preview(showBackground = true)
@Composable
fun PreviewPasswordTextField() {
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    PasswordTextField(
        password = password,
        onPasswordChange = { password = it },
        label = "Password",
        isPasswordVisible = isPasswordVisible,
        onVisibilityChange = { isPasswordVisible = !isPasswordVisible }
    )
}