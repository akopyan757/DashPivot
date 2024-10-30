package com.cheesecake.dashpivot.entry

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Registration : Screen("registration")
}