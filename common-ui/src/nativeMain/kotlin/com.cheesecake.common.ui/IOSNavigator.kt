package com.cheesecake.common.ui

import com.cheesecake.common.ui.navigator.DialogNavigator
import com.cheesecake.common.ui.navigator.RegularScreen
import platform.UIKit.UINavigationController
import platform.UIKit.UIStoryboard
import platform.UIKit.restorationIdentifier

class IOSNavigator(private val navigationController: UINavigationController): DialogNavigator() {

    override fun navigateTo(screen: RegularScreen) {
        val storyboard = UIStoryboard.storyboardWithName(name = screen.storyBoardName, bundle = null)
        val nextViewController = storyboard.instantiateViewControllerWithIdentifier(screen.fullRoute)
        nextViewController.restorationIdentifier = screen.fullRoute
        navigationController.pushViewController(nextViewController, animated = true)
        println("Storyboard: navigateTo = ${screen.fullRoute}")
    }

    override fun goBack() {
        navigationController.popViewControllerAnimated(true)
        println("Storyboard: goBack")
    }

    override fun showErrorMessage(message: String) {
        println("showErrorMessage: message: $message")
    }
}