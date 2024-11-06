package com.cheesecake.auth.feature

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cheesecake.auth.feature.verification.VerificationCodeScreen
import com.cheesecake.auth.feature.verification.VerificationFormData
import com.cheesecake.auth.feature.verification.VerificationLogicState
import com.cheesecake.auth.feature.verification.VerificationState

private const val DEFAULT_EMAIL = "test@example.com"
private const val DEBUG_MODE = true

@Preview(showBackground = true)
@Composable
fun PreviewVerificationScreen_Idle() {
    MaterialTheme {
        VerificationCodeScreen(
            modifier = Modifier.fillMaxSize(),
            email = DEFAULT_EMAIL,
            state = VerificationState(),
            onCodeCompleted = {},
            isDebugMode = DEBUG_MODE,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVerificationScreen_StartedCode() {
    MaterialTheme {
        VerificationCodeScreen(
            modifier = Modifier.fillMaxSize(),
            email = DEFAULT_EMAIL,
            state = VerificationState(),
            onCodeCompleted = {},
            startedCode = "123",
            isDebugMode = DEBUG_MODE,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVerificationScreen_NotAvailableResendCode() {
    MaterialTheme {
        VerificationCodeScreen(
            modifier = Modifier.fillMaxSize(),
            email = DEFAULT_EMAIL,
            state = VerificationState(formData = VerificationFormData(60)),
            onCodeCompleted = {},
            isDebugMode = DEBUG_MODE,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVerificationScreen_Success() {
    MaterialTheme {
        VerificationCodeScreen(
            modifier = Modifier.fillMaxSize(),
            email = DEFAULT_EMAIL,
            state = VerificationState(logicState = VerificationLogicState.Success),
            onCodeCompleted = {},
            isDebugMode = DEBUG_MODE,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVerificationScreen_Error() {
    MaterialTheme {
        VerificationCodeScreen(
            modifier = Modifier.fillMaxSize(),
            email = DEFAULT_EMAIL,
            state = VerificationState(logicState =
                VerificationLogicState.Error("Error message")
            ),
            onCodeCompleted = {},
            isDebugMode = DEBUG_MODE,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVerificationScreen_Loading() {
    MaterialTheme {
        VerificationCodeScreen(
            modifier = Modifier.fillMaxSize(),
            email = DEFAULT_EMAIL,
            state =  VerificationState(logicState = VerificationLogicState.Loading),
            onCodeCompleted = {},
            isDebugMode = DEBUG_MODE,
        )
    }
}