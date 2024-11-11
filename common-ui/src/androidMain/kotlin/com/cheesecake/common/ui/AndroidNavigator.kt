package com.cheesecake.common.ui

import android.widget.Toast
import androidx.navigation.NavHostController
import com.cheesecake.common.ui.navigator.DialogNavigator
import com.cheesecake.common.ui.navigator.RegularScreen
import com.cheesecake.common.ui.navigator.state.fullRouteWithArgs

class AndroidNavigator(
    private val navController: NavHostController,
) : DialogNavigator() {

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

    override fun showToastMessage(message: String) {
        Toast.makeText(navController.context, message, Toast.LENGTH_SHORT).show()
    }
}