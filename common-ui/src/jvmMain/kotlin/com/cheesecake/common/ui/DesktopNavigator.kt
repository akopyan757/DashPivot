package com.cheesecake.common.ui

import com.cheesecake.common.ui.navigator.DialogNavigator
import com.cheesecake.common.ui.navigator.RegularScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class DesktopNavigator(startScreen: RegularScreen): DialogNavigator() {

    private val _screenStack = MutableStateFlow(listOf(startScreen))
    val currentScreen: Flow<RegularScreen?> = _screenStack.map { it.lastOrNull() }

    override fun navigateTo(screen: RegularScreen) {
        _screenStack.value += screen
    }

    override fun goBack() {
        _screenStack.value = _screenStack.value.dropLast(1)
    }

    override fun goBack(to: RegularScreen) {
        _screenStack.value = _screenStack.value.dropLastWhile { it != to }
    }

    override fun showToastMessage(message: String) {
        super.showToastMessage(message)
    }
}