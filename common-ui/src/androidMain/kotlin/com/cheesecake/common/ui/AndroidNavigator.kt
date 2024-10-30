package com.cheesecake.common.ui

import android.widget.Toast
import androidx.navigation.NavHostController
import com.cheesecake.common.ui.navigator.DialogNavigator
import com.cheesecake.common.ui.navigator.DialogScreen
import com.cheesecake.common.ui.navigator.RegularScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AndroidNavigator(
    private val navController: NavHostController,
) : DialogNavigator() {

    private var currentStoryBoardName: String? = null

    private val _currentDialog = MutableStateFlow<DialogScreen?>(null)
    override val currentDialog: StateFlow<DialogScreen?> = _currentDialog

    override fun navigateTo(screen: RegularScreen) {
        if (currentStoryBoardName != screen.storyBoardName) {
            navController.popBackStack(navController.graph.startDestinationId, true)
        }
        navController.navigate(screen.fullRoute)
        currentStoryBoardName = screen.storyBoardName
    }

    override fun goBack() {
        navController.popBackStack()
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(navController.context, message, Toast.LENGTH_SHORT).show()
    }
}