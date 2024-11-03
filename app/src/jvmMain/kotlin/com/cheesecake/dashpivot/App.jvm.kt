package com.cheesecake.dashpivot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cheesecake.auth.feature.di.JvmKoinComponent


@Composable
actual fun App() {
    val navigatorHost = JvmKoinComponent().getNavigatorHost()

    Box(modifier = Modifier.fillMaxSize().padding(WindowInsets.ime.asPaddingValues())) {
        navigatorHost.Screen()
    }
}