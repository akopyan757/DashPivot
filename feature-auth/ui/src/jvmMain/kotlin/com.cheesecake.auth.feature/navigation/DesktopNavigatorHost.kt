package com.cheesecake.auth.feature.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.cheesecake.auth.feature.di.JvmKoinComponent
import com.cheesecake.common.ui.DesktopNavigator
import com.cheesecake.common.ui.navigator.DialogScreen
import com.cheesecake.common.ui.navigator.Navigator
import com.cheesecake.common.ui.navigator.NavigatorHost
import com.cheesecake.common.ui.navigator.RegularScreen


class DesktopNavigatorHost : NavigatorHost {

    private val desktopNavigator = DesktopNavigator(AuthScreen.Login)
    override val navigator: Navigator = desktopNavigator

    @Composable
    override fun Screen() {
        val regularScreen: RegularScreen? by desktopNavigator.currentScreen.collectAsState(null)
        val dialogScreen: DialogScreen? by desktopNavigator.currentDialog.collectAsState()
        val jvmKoinComponent = JvmKoinComponent()

        Box(Modifier.fillMaxSize()) {
            regularScreen?.let {
                getComposable(it, navigator, jvmKoinComponent)
            }
            dialogScreen?.let {
                getComposable(it, navigator, jvmKoinComponent)
            }
        }
    }
}