package com.cheesecake.auth.feature.navigation

import com.cheesecake.common.ui.navigator.DialogScreen
import com.cheesecake.common.ui.navigator.RegularScreen
import com.cheesecake.common.ui.navigator.Screen
/**
 * Sealed class representing authentication screens.
 *
 * @property route The route of the screen.
 */
sealed class AuthScreen(override val route: String): Screen {

    override val storyBoardName: String = "Auth"

    /**
     * Object representing the login screen.
     */
    data object Login : AuthScreen("login"), RegularScreen

    /**
     * Object representing the registration screen.
     */
    data object Registration : AuthScreen("registration"), RegularScreen

    /**
     * Data class representing the verification screen.
     *
     * @property token The verification token.
     */
    data class Verification(val token: String? = null) : AuthScreen("verification"), DialogScreen

    companion object {
        private fun values() = listOf<RegularScreen>(Login, Registration)

        fun regularScreenOfRoute(route: String): RegularScreen? {
            return values().firstOrNull { it.fullRoute == route }
        }
    }
}