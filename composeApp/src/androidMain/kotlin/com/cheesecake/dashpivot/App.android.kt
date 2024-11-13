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
import com.cheesecake.common.ui.navigator.NavigatorHost
import com.cheesecake.common.ui.toast.ToastMessage
import com.cheesecake.common.ui.toast.ToastSurface
import org.koin.compose.getKoin
import org.koin.core.scope.Scope

@Composable
actual fun App() {
    val scope: Scope? = getKoin().getScopeOrNull("MainActivityScope")
    val navigatorHost: NavigatorHost? = scope?.get()

    navigatorHost?.let {
        val toast: ToastMessage? by it.navigator.toastMessage.collectAsState()
        Box(modifier = Modifier.fillMaxSize().padding(WindowInsets.ime.asPaddingValues())) {
            navigatorHost.Screen()
            ToastSurface(
                modifier = Modifier.align(BiasAlignment(0.0f, 0.75f)),
                toastMessage = toast ?: ToastMessage.Idle,
                onReset = { navigatorHost.navigator.dismissToastMessage() }
            )
        }
    }
}