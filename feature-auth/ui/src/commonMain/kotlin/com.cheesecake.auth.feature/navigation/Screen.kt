package com.cheesecake.auth.feature.navigation

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
    class Verification(block: (key: String) -> String? = { null }) : AuthScreen("verification"), RegularScreen {

        val email: String
            get() = _arguments[EMAIL_KEY].orEmpty()

        private val _arguments =  hashMapOf<String, String>().apply {
            keys.forEach { put(it, "") }
        }
        override val arguments: Map<String, String> get() = _arguments
        override val keys: Array<String> = _keys

        init {
            keys.forEach { key ->
                block(key)?.let { value ->
                    _arguments[key] = value
                }
            }
        }

        constructor(email: String) : this({ email })

        companion object {
            private const val EMAIL_KEY = "email"
            private val _keys = arrayOf(EMAIL_KEY)
        }
    }

    companion object {
        private fun values() = listOf<RegularScreen>(Login, Registration, Verification())

        fun regularScreenOfRoute(route: String): RegularScreen? {
            return values().firstOrNull { it.fullRoute == route }
        }
    }
}