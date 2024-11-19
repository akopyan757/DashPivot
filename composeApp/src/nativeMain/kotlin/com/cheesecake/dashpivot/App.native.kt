package com.cheesecake.dashpivot

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cheesecake.auth.feature.di.NativeKoinComponent
import com.cheesecake.common.ui.toast.ToastMessage
import com.cheesecake.common.ui.toast.ToastSurface
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
fun getStatusBarHeight(): Int {
    val window = UIApplication.sharedApplication.keyWindow
    val statusBarHeight = window?.safeAreaInsets?.useContents { top } ?: 0.0
    return statusBarHeight.toInt()
}

@Composable
actual fun App() {
    val navigatorHost = NativeKoinComponent().getNavigatorHost()
    val toast: ToastMessage by navigatorHost.navigator.toastMessage.collectAsState()

    Column {
        Spacer(Modifier.padding(top = getStatusBarHeight().dp))
        Box(modifier = Modifier.fillMaxSize().padding(WindowInsets.ime.asPaddingValues())) {
            navigatorHost.Screen()
            ToastSurface(
                modifier = Modifier.align(BiasAlignment(0.0f, 0.75f)),
                toastMessage = toast,
                borderStroke = BorderStroke(0.5.dp, Color.Gray),
                onReset = { navigatorHost.navigator.dismissToastMessage() }
            )
        }
    }
}