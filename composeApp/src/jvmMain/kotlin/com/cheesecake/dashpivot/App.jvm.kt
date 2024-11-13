package com.cheesecake.dashpivot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import com.cheesecake.auth.feature.di.JvmKoinComponent
import com.cheesecake.common.ui.toast.ToastMessage
import com.cheesecake.common.ui.toast.ToastSurface


@Composable
actual fun App() {
    val navigatorHost = JvmKoinComponent().getNavigatorHost()
    val toast: ToastMessage by navigatorHost.navigator.toastMessage.collectAsState()

    Box(modifier = Modifier.fillMaxSize().padding(WindowInsets.ime.asPaddingValues())) {
        navigatorHost.Screen()
        ToastSurface(
            modifier = Modifier.align(BiasAlignment(0.0f, 0.75f)),
            toastMessage = toast,
            onReset = { navigatorHost.navigator.dismissToastMessage() }
        )
    }
}