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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cheesecake.auth.feature.di.AppKoinComponent
import com.cheesecake.common.auth.config.Config.VERIFICATION_CODE_COUNT
import com.cheesecake.common.ui.colors.BlueColor
import com.cheesecake.common.ui.colors.BlueLightColor
import com.cheesecake.common.ui.colors.GreenColor
import kotlinx.coroutines.delay

@Composable
fun VerificationScreen(
    appKoinComponent: AppKoinComponent,
    email: String,
    onSuccessFinished: () -> Unit,
    onBackPressed: () -> Unit = {},
) {
    VerificationScreen(
        appKoinComponent.getVerificationViewModel(), email, onSuccessFinished, onBackPressed
    )
}

@Composable
fun VerificationScreen(
    viewModel: VerificationViewModel,
    email: String = "",
    onSuccessFinished: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    val verificationState by viewModel.verificationState.collectAsState()
    val logicState = verificationState.logicState

    LaunchedEffect(email) {
        viewModel.resetTimer()
    }

    LaunchedEffect(logicState) {
        if (logicState is VerificationLogicState.Success) {
            onSuccessFinished()
            viewModel.resetToIdle()
        }
    }

    VerificationCodeScreen(
        modifier = Modifier.fillMaxSize(),
        email = email,
        state = verificationState,
        onCodeCompleted = { viewModel.verifyToken(email, it) },
        onResetToIdle = { viewModel.resetToIdle() },
        onResendCode = { viewModel.resendCode(email) },
        onBackPressed = {
            onBackPressed()
            viewModel.resetToIdle()
        },
    )
}

@Composable
fun VerificationCodeScreen(
    modifier: Modifier = Modifier,
    email: String = "",
    state: VerificationState = VerificationState(),
    onBackPressed: () -> Unit = {},
    onResetToIdle: () -> Unit = {},
    onCodeCompleted: (String) -> Unit = {},
    onResendCode: () -> Unit = {},
    startedCode: String = "",
    isDebugMode: Boolean = false,
) {
    var code by remember(isDebugMode) { mutableStateOf(if (isDebugMode) startedCode else "") }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember(isDebugMode) { mutableStateOf(isDebugMode && startedCode.isNotEmpty()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val logicState = state.logicState
    val isVerificationLoading = logicState is VerificationLogicState.Loading
    val isVerificationSuccess = logicState is VerificationLogicState.Success

    var currentTimer by remember(state.formData) { mutableStateOf(state.formData.restTime) }
    val isResendAvailable by derivedStateOf {
        currentTimer < 0 && !isVerificationSuccess && !state.formData.isResendLoading
    }

    LaunchedEffect(state.formData.restTime) {
        while (!isResendAvailable) {
            delay(1000)
            currentTimer--
        }
    }

    LaunchedEffect(email) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Box {
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter Verification Code",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "A $VERIFICATION_CODE_COUNT-digit verification code has been sent to $email. " +
                        "Please check your email and enter the code in the input field.",
                modifier = Modifier.padding(horizontal = 24.dp).widthIn(max = 350.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(VERIFICATION_CODE_COUNT) { index ->
                    val borderColor = when {
                        logicState is VerificationLogicState.Error -> MaterialTheme.colorScheme.error
                        logicState is VerificationLogicState.Success -> GreenColor
                        index == code.length && isFocused -> BlueLightColor
                        index < code.length && isFocused -> BlueColor
                        else -> Color.LightGray
                    }
                    val shape = RoundedCornerShape(8.dp)
                    Box(
                        modifier = Modifier
                            .size(width = 40.dp, height = 60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                            .border(width = 2.dp, color = borderColor, shape)
                            .clickable {
                                focusRequester.requestFocus()
                                keyboardController?.show()
                            },
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
                onValueChange = { newCode ->
                    if (newCode.length <= VERIFICATION_CODE_COUNT) {
                        code = newCode
                    }
                    if (newCode.length < VERIFICATION_CODE_COUNT) {
                        if (logicState !is VerificationLogicState.Idle) {
                            onResetToIdle()
                        }
                    } else if (newCode.length == VERIFICATION_CODE_COUNT) {
                        onCodeCompleted(code)
                        println("VerificationCodeScreen: hide: onValueChange: newCode.length == VERIFICATION_CODE_COUNT")
                        keyboardController?.hide()
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .let {
                        if (!isDebugMode) {
                            it.onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                                println("VerificationCodeScreen: isFocused = $isFocused")
                            }
                        } else it
                    }
                    .background(Color.Transparent)
                    .width(1.dp)
                    .height(1.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = {
                    println("VerificationCodeScreen: hide: keyboardActions = KeyboardActions(onDone)")
                    keyboardController?.hide()
                }),
                textStyle = TextStyle(fontSize = 0.sp, color = Color.Transparent)
            )

            if (isVerificationLoading) {
                VerificationLoading(message = "Verifying, please wait...")
            }

            if (isVerificationSuccess) {
                VerificationSuccess(
                    message = "Verification successful!\nWelcome"
                )
            }

            if (logicState is VerificationLogicState.Error) {
                VerificationError(message = logicState.message)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                enabled = isResendAvailable && !isVerificationLoading,
                onClick = {
                    onResendCode()
                    keyboardController?.hide()
                },
            ) {
                if (isResendAvailable) {
                    Text(
                        text = "Resend New Code",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                    )
                } else {
                    val text = when {
                        isVerificationSuccess -> "Verification successful"
                        state.formData.isResendLoading -> "Resending code, please wait..."
                        else -> "Resend New Code in $currentTimer seconds"
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                    )
                }
            }

        }
    }
}

@Composable
fun VerificationLoading(message: String) {
    Text(
        text = message,
        textAlign = TextAlign.Center,
        color = Color.Black,
        style = MaterialTheme.typography.bodyMedium,
    )

    Spacer(modifier = Modifier.height(8.dp))

    CircularProgressIndicator(
        color = Color.Gray,
        strokeWidth = 2.dp,
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun VerificationSuccess(message: String, ) {
    Text(
        text = message,
        color = GreenColor,
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