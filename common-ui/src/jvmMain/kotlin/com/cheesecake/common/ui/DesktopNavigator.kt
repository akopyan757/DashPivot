package com.cheesecake.common.ui

import com.cheesecake.common.ui.navigator.DialogScreen
import com.cheesecake.common.ui.navigator.Navigator
import com.cheesecake.common.ui.navigator.RegularScreen
import com.cheesecake.common.ui.toast.ToastMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class DesktopNavigator(startScreen: RegularScreen): Navigator {

    private val _screenStack = MutableStateFlow(listOf(startScreen))
    val currentScreen: Flow<RegularScreen?> = _screenStack.map { it.lastOrNull() }

    private val _currentDialog = MutableStateFlow<DialogScreen?>(null)
    override val currentDialog: StateFlow<DialogScreen?> = _currentDialog

    private val _toastMessage = MutableStateFlow<ToastMessage>(ToastMessage.Idle)
    override val toastMessage: StateFlow<ToastMessage> = _toastMessage

    override fun navigateTo(screen: RegularScreen) {
        _screenStack.value += screen
    }

    override fun goBack() {
        _screenStack.value = _screenStack.value.dropLast(1)
    }

    override fun goBack(to: RegularScreen) {
        _screenStack.value = _screenStack.value.dropLastWhile { it != to }
    }

    override fun showToastMessage(toast: ToastMessage) = runBlocking {
        if (toast is ToastMessage.Idle) return@runBlocking
        _toastMessage.value = toast
    }

    override fun dismissToastMessage() {
        _toastMessage.value = ToastMessage.Idle
    }
}