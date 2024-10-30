package com.cheesecake.auth.feature.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cheesecake.auth.feature.di.AppKoinComponent

@Composable
fun VerificationScreen(
    appKoinComponent: AppKoinComponent,
    token: String,
    onDismiss: () -> Unit,
) {
    VerificationScreen(appKoinComponent.getVerificationViewModel(), token, onDismiss)
}

@Composable
fun VerificationScreen(
    viewModel: VerificationViewModel,
    token: String,
    onDismiss: () -> Unit
) {
    val verificationState by viewModel.verificationState.collectAsState()

    LaunchedEffect(token) {
        viewModel.verifyToken(token)
    }

    if (verificationState is VerificationState.Loading) {
        Overlay(message = "Verifying, please wait...")
    }

    when (verificationState) {
        is VerificationState.Success -> {
            SuccessDialog(
                message = "Verification successful! Welcome.",
                onDismiss = onDismiss
            )
        }
        is VerificationState.Error -> {
            ErrorDialog(
                message = (verificationState as VerificationState.Error).message,
                onDismiss = onDismiss
            )
        }
        else -> Unit
    }
}


@Composable
fun Overlay(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shadow(4.dp, clip = false)
            .background(Color.Gray.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
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
}

@Composable
fun SuccessDialog(
    message: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shadow(4.dp, clip = false)
            .background(Color.Gray.copy(alpha = 0.3f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
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
                color = Color.Gray,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onDismiss) {
                Text(
                    "Dismiss",
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shadow(4.dp, clip = false)
            .background(Color.Gray.copy(alpha = 0.3f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
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
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onDismiss) {
                Text(
                    "Dismiss",
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}