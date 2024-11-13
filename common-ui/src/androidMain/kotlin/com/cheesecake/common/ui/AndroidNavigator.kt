package com.cheesecake.common.ui

import androidx.navigation.NavHostController
import com.cheesecake.common.ui.navigator.DialogScreen
import com.cheesecake.common.ui.navigator.Navigator
import com.cheesecake.common.ui.navigator.RegularScreen
import com.cheesecake.common.ui.navigator.state.fullRouteWithArgs
import com.cheesecake.common.ui.toast.ToastMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AndroidNavigator(
    private val navController: NavHostController,
) : Navigator {

    private val _currentDialog = MutableStateFlow<DialogScreen?>(null)
    override val currentDialog: StateFlow<DialogScreen?> = _currentDialog

    private val _toastMessage = MutableStateFlow<ToastMessage>(ToastMessage.Idle)
    override val toastMessage: StateFlow<ToastMessage> = _toastMessage

    private var currentStoryBoardName: String? = null

    override fun navigateTo(screen: RegularScreen) {
        if (currentStoryBoardName != screen.storyBoardName) {
            navController.popBackStack(navController.graph.startDestinationId, true)
        }
        navController.navigate(screen.fullRouteWithArgs)
    }

    override fun goBack() {
        navController.popBackStack()
    }

    override fun goBack(to: RegularScreen) {
        navController.popBackStack(to.fullRouteWithArgs, false)
    }

    override fun showToastMessage(toast: ToastMessage) {
        _toastMessage.value = toast
    }

    override fun dismissToastMessage() {
        _toastMessage.value = ToastMessage.Idle
    }
}