package com.cheesecake.common.ui.navigator

import androidx.compose.runtime.Composable

interface NavigatorHost {

    val navigator: Navigator

    @Composable
    fun Screen()
}