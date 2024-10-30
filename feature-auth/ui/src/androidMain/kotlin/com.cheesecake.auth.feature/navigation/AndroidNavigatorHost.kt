package com.cheesecake.auth.feature.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cheesecake.auth.feature.di.AndroidKoinComponent
import com.cheesecake.auth.feature.events.VerifyEmailEvent
import com.cheesecake.common.ui.events.EventStateHolder
import com.cheesecake.common.ui.navigator.Navigator
import com.cheesecake.common.ui.navigator.NavigatorHost

class AndroidNavigatorHost(
    private val navController: NavHostController,
    override val navigator: Navigator,
    private val eventStateHolder: EventStateHolder<VerifyEmailEvent>,
) : NavigatorHost {

    @Composable
    override fun Screen() {
        val koinComponent = AndroidKoinComponent()
        val dialogScreen by navigator.currentDialog.collectAsState()
        val verificationToken by eventStateHolder.appEntryState.collectAsState()

        LaunchedEffect(verificationToken) {
            verificationToken?.let {
                navigator.showDialog(AuthScreen.Verification(it.token))
            }
        }

        Box {
            NavHost(navController = navController, startDestination = AuthScreen.Login.fullRoute) {
                composable(AuthScreen.Login.fullRoute) {
                    getComposable(AuthScreen.Login, navigator, koinComponent)
                }
                composable(AuthScreen.Registration.fullRoute) {
                    getComposable(AuthScreen.Registration, navigator, koinComponent)
                }
            }
            dialogScreen?.let { getComposable(it, navigator, koinComponent) }
        }
    }
}