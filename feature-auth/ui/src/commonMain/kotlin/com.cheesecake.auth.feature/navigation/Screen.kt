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
     * @property email The email to verify.
     * @property code The verification code.
     */
    data class Verification(val email: String = "") : AuthScreen("verification"), RegularScreen

    companion object {
        private fun values() = listOf<RegularScreen>(Login, Registration)

        fun regularScreenOfRoute(route: String): RegularScreen? {
            return values().firstOrNull { it.fullRoute == route }
        }
    }
}