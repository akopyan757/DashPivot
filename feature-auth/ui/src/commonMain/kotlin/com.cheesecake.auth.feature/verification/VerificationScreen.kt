package com.cheesecake.auth.feature.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cheesecake.auth.feature.di.AppKoinComponent
import com.cheesecake.common.auth.config.Config.VERIFICATION_CODE_COUNT

@Composable
fun VerificationScreen(
    appKoinComponent: AppKoinComponent,
    email: String,
    onSuccessFinished: () -> Unit,
) {
    VerificationScreen(appKoinComponent.getVerificationViewModel(), email, onSuccessFinished)
}

@Composable
fun VerificationScreen(
    viewModel: VerificationViewModel,
    email: String,
    onSuccessFinished: () -> Unit,
) {
    val verificationState by viewModel.verificationState.collectAsState()

    LaunchedEffect(verificationState) {
        if (verificationState is VerificationState.Success) {
            onSuccessFinished()
        }
    }

    VerificationCodeScreen(
        modifier = Modifier.fillMaxSize(),
        state = verificationState,
        onCodeCompleted = { viewModel.verifyToken(email, it) }
    )
}

@Composable
private fun VerificationCodeScreen(
    modifier: Modifier = Modifier,
    state: VerificationState = VerificationState.Idle,
    onCodeCompleted: (String) -> Unit = {},
) {
    var code by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }


    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Enter Verification Code", fontSize = 20.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(VERIFICATION_CODE_COUNT) { index ->
                val borderColor = when {
                    index == code.length && isFocused -> Color.Yellow.copy(alpha = 0.5f)
                    index < code.length  && isFocused -> Color.Blue.copy(alpha = 0.5f)
                    else -> Color.LightGray
                }
                val shape = RoundedCornerShape(8.dp)
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .border(width = 2.dp, color = borderColor, shape)
                        .clickable { focusRequester.requestFocus() },
                    contentAlignment = Alignment.Center
                ) {
                    val char = if (index < code.length) code[index].toString() else ""
                    Text(text = char, style = TextStyle(fontSize = 24.sp, color = Color.Black))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = code,
            onValueChange = {
                if (it.length <= VERIFICATION_CODE_COUNT) {
                    code = it
                }
                if (code.length == VERIFICATION_CODE_COUNT) {
                    onCodeCompleted(code)
                    keyboardController?.hide()
                }
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused = it.isFocused }
                .background(Color.Transparent)
                .width(1.dp)
                .height(1.dp),
            textStyle = TextStyle(fontSize = 0.sp, color = Color.Transparent)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state is VerificationState.Loading) {
            VerificationLoading(message = "Verifying, please wait...")
        }

        if (state is VerificationState.Success) {
            VerificationSuccess(message = "Verification successful!\nWelcome")
        }

        if (state is VerificationState.Error) {
            VerificationError(message = state.message)
        }
    }
}

@Composable
fun VerificationLoading(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .wrapContentWidth()
            .widthIn(min=250.dp)
            .padding(24.dp)
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        CircularProgressIndicator(
            color = Color.Gray,
            strokeWidth = 2.dp,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun VerificationSuccess(message: String, ) {
    Text(
        text = message,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
fun VerificationError(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
    )
}