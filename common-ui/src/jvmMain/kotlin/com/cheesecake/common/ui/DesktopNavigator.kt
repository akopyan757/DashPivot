package com.cheesecake.common.ui

import com.cheesecake.common.ui.navigator.DialogNavigator
import com.cheesecake.common.ui.navigator.RegularScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DesktopNavigator(startScreen: RegularScreen): DialogNavigator() {

    private val _currentScreen = MutableStateFlow(startScreen)
    val currentScreen: StateFlow<RegularScreen> = _currentScreen

    override fun navigateTo(screen: RegularScreen) {
        _currentScreen.value = screen
    }

    override fun goBack() {}

    override fun goBack(to: RegularScreen) {
        _currentScreen.value = to
    }

    override fun showErrorMessage(message: String) {
        super.showErrorMessage(message)
    }
}