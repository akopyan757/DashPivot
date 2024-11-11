package com.cheesecake.common.ui.navigator.state

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.cheesecake.common.ui.navigator.Screen

val <T: Screen> T.fullRouteWithKeys: String
    get() = fullRoute + keys.joinToString(separator = "") { key -> "/{${key}}" }

val <T: Screen> T.fullRouteWithArgs: String
    get() = fullRoute + keys.joinToString(separator = "") { key -> "/${arguments[key]}" }

val <T: Screen> T.navNamedArguments: List<NamedNavArgument>
    get() = arguments.map { navArgument(it.key) { type = NavType.StringType } }