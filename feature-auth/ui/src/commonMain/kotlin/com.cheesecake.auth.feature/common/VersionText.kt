package com.cheesecake.auth.feature.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cheesecake.common.api.VersionInfo

@Composable
fun VersionText(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = "Client version: ${VersionInfo.CLIENT_VERSION}\n" +
                "Server version: ${VersionInfo.SERVER_VERSION}",
        style = MaterialTheme.typography.bodyMedium
    )
}