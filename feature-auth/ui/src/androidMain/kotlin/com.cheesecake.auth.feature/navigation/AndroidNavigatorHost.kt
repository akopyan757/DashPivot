package com.cheesecake.auth.feature.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cheesecake.auth.feature.di.AndroidKoinComponent
import com.cheesecake.common.ui.navigator.Navigator
import com.cheesecake.common.ui.navigator.NavigatorHost
import com.cheesecake.common.ui.navigator.state.fullRouteWithKeys
import com.cheesecake.common.ui.navigator.state.navNamedArguments

class AndroidNavigatorHost(
    private val navController: NavHostController,
    override val navigator: Navigator,
) : NavigatorHost {

    @Composable
    override fun Screen() {
        val koinComponent = AndroidKoinComponent
        Box {
            NavHost(navController = navController, startDestination = AuthScreen.Login.fullRoute) {
                println("NavHost: " + AuthScreen.Login.fullRouteWithKeys)
                composable(AuthScreen.Login.fullRouteWithKeys) {
                    getComposable(AuthScreen.Login, navigator, koinComponent)
                }
                println("NavHost: " + AuthScreen.Registration.fullRouteWithKeys)
                composable(AuthScreen.Registration.fullRouteWithKeys) {
                    getComposable(AuthScreen.Registration, navigator, koinComponent)
                }
                println("NavHost: " + AuthScreen.Verification().fullRouteWithKeys)
                composable(
                    route = AuthScreen.Verification().fullRouteWithKeys,
                    arguments = AuthScreen.Verification().navNamedArguments,
                ) { backStackEntry ->
                    getComposable(AuthScreen.Verification { key ->
                        backStackEntry.arguments?.getString(key).orEmpty()
                    }, navigator, koinComponent)
                }
            }
        }
    }
}