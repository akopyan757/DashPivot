package com.cheesecake.auth.feature.ui

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
import com.cheesecake.auth.feature.ui.version.VersionText
import com.cheesecake.auth.feature.viewmodel.SignUpState
import com.cheesecake.auth.feature.viewmodel.SignUpViewModel

@Composable
expect fun SignUpScreen()

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    val signUpState by viewModel.signUpState.collectAsState()

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
            Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            EmailTextField(
                email = email,
                onEmailChange = {
                    email = it
                    viewModel.onResetEmailError()
                },
                label = "Email",
                errorMessage = (signUpState as? SignUpState.Error)?.emailErrorMessage
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordTextField(
                password = password,
                onPasswordChange = {
                    password = it
                    viewModel.onResetPasswordError()
                },
                label = "Create password",
                isPasswordVisible = isPasswordVisible,
                onVisibilityChange = { isPasswordVisible = !isPasswordVisible },
                errorMessage = (signUpState as? SignUpState.Error)?.passwordMessage
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordTextField(
                password = confirmPassword,
                onPasswordChange = {
                    confirmPassword = it
                    viewModel.onResetConfirmationPasswordError()
                },
                label = "Confirm password",
                isPasswordVisible = isConfirmPasswordVisible,
                onVisibilityChange = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
                errorMessage = (signUpState as? SignUpState.Error)?.confirmPasswordMessage
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.signUp(email, password, confirmPassword) },
                modifier = Modifier.fillMaxWidth(),
                enabled = signUpState !is SignUpState.Loading
            ) {
                if (signUpState is SignUpState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Sign Up")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (signUpState is SignUpState.Error && (signUpState as SignUpState.Error).error != null) {
                Text(
                    text = (signUpState as SignUpState.Error).error?.message.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
            } else if (signUpState is SignUpState.Success) {
                Text(
                    text = (signUpState as SignUpState.Success).message,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        VersionText(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        )
    }
}