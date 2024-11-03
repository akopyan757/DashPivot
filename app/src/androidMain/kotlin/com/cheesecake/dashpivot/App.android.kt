package com.cheesecake.dashpivot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cheesecake.common.ui.navigator.NavigatorHost
import org.koin.compose.getKoin
import org.koin.core.scope.Scope

@Composable
actual fun App() {
    val scope: Scope? = getKoin().getScopeOrNull("MainActivityScope")
    val navigatorHost: NavigatorHost? = scope?.get()
    navigatorHost?.let {
        Box(modifier = Modifier.fillMaxSize().padding(WindowInsets.ime.asPaddingValues())) {
            navigatorHost.Screen()
        }
    }
}