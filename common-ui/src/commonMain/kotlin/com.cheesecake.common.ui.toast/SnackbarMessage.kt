package com.cheesecake.common.ui.toast

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ToastSurface(
    modifier: Modifier = Modifier,
    toastMessage: ToastMessage = ToastMessage.Idle,
    duration: Long = 2000L,
    onReset: () -> Unit = {},
    borderStroke: BorderStroke? = null,
) {
    val isVisibility = toastMessage !is ToastMessage.Idle

    LaunchedEffect(isVisibility) {
        if (isVisibility) {
            delay(duration)
            onReset()
        }
    }

    if (isVisibility) {
        val message = when (toastMessage) {
            is ToastMessage.Info -> toastMessage.message
            is ToastMessage.Error -> toastMessage.message
            else -> ""
        }
        val color = when (toastMessage) {
            is ToastMessage.Info -> Color.Black
            is ToastMessage.Error -> MaterialTheme.colors.error
            else -> Color.Transparent
        }
        val icon = when (toastMessage) {
            is ToastMessage.Error -> Icons.Default.Warning
            else -> null
        }
        Surface(
            modifier = modifier.wrapContentSize(),
            color = MaterialTheme.colors.surface,
            shape = RoundedCornerShape(16.dp),
            border = borderStroke?.copy(brush = SolidColor(color)),
            elevation = 4.dp,
        ) {
            Row(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = null, tint = color)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = message,
                    color = color,
                    maxLines = 5,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp
                )
            }
        }
    }
}