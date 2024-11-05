package com.cheesecake.auth.feature.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        Box {
            NavHost(navController = navController, startDestination = AuthScreen.Login.fullRoute) {
                composable(AuthScreen.Login.fullRoute) {
                    getComposable(AuthScreen.Login, navigator, koinComponent)
                }
                composable(AuthScreen.Registration.fullRoute) {
                    getComposable(AuthScreen.Registration, navigator, koinComponent)
                }
                composable(
                    route = AuthScreen.Verification().fullRoute,
                    arguments = listOf(navArgument(AuthScreen.Verification.EMAIL_KEY) { type = NavType.StringType }),
                ) { backStackEntry ->
                    val email = backStackEntry.arguments?.getString(AuthScreen.Verification.EMAIL_KEY).orEmpty()
                    getComposable(AuthScreen.Verification(email), navigator, koinComponent)
                }
            }
        }
    }
}