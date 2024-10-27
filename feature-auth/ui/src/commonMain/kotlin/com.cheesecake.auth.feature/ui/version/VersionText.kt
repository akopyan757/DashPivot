package com.cheesecake.auth.feature.ui.version

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cheesecake.common.api.VERSION

@Composable
fun VersionText(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = "Version: $VERSION",
        style = MaterialTheme.typography.bodyMedium
    )
}