package com.cheesecake.auth.feature.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cheesecake.auth.feature.common.EmailTextField
import com.cheesecake.auth.feature.common.PasswordTextField
import com.cheesecake.auth.feature.common.VersionText
import com.cheesecake.auth.feature.di.AppKoinComponent

interface LoginNavigate {
    fun toRegistration() {}
}

@Composable
fun LoginScreen(appKoinComponent: AppKoinComponent, loginNavigate: LoginNavigate) {
    LoginScreen(appKoinComponent.getLoginViewModel(), loginNavigate)
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    loginNavigate: LoginNavigate,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val loginState by viewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Sign In", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            EmailTextField(
                email = email,
                onEmailChange = {
                    email = it
                    viewModel.onResetEmailError()
                },
                label = "Email",
                errorMessage = (loginState as? LoginState.Error)?.emailErrorMessage
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordTextField(
                password = password,
                onPasswordChange = {
                    password = it
                    viewModel.onResetPasswordError()
                },
                label = "Password",
                isPasswordVisible = isPasswordVisible,
                onVisibilityChange = { isPasswordVisible = !isPasswordVisible },
                errorMessage = (loginState as? LoginState.Error)?.passwordMessage
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is LoginState.Loading
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Sign In")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (loginState is LoginState.Error && (loginState as LoginState.Error).error != null) {
                Text(
                    text = (loginState as LoginState.Error).error?.message.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
            } else if (loginState is LoginState.Success) {
                Text(
                    text = (loginState as LoginState.Success).message,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Don't have an account?",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
                    .clickable {
                        loginNavigate.toRegistration()
                    }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        VersionText(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        )
    }
}