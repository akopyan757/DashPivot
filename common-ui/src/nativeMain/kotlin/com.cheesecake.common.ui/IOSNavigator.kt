package com.cheesecake.common.ui

import com.cheesecake.common.ui.events.EventStateHolder
import com.cheesecake.common.ui.navigator.DialogNavigator
import com.cheesecake.common.ui.navigator.RegularScreen
import platform.UIKit.UINavigationController
import platform.UIKit.UIStoryboard
import platform.UIKit.UIViewController
import platform.UIKit.restorationIdentifier

class IOSNavigator(
    private val navigationController: UINavigationController,
    private val eventStateHolder: EventStateHolder,
): DialogNavigator() {

    override fun navigateTo(screen: RegularScreen) {
        val storyboard = UIStoryboard.storyboardWithName(name = screen.storyBoardName, bundle = null)
        val nextViewController = storyboard.instantiateViewControllerWithIdentifier(screen.fullRoute)
        println("IOSNavigator: navigateTo: route = ${screen.fullRoute}")
        println("IOSNavigator: navigateTo: arguments = ${screen.arguments}")
        screen.arguments.forEach { (key, value) -> eventStateHolder.put(key, value) }
        nextViewController.restorationIdentifier = screen.fullRoute
        navigationController.pushViewController(nextViewController, animated = true)
    }

    override fun goBack() {
        navigationController.popViewControllerAnimated(true)
        println("IOSNavigator: goBack")
    }

    override fun goBack(to: RegularScreen) {
        val prevViewController = navigationController.viewControllers
            .mapNotNull { it as? UIViewController }
            .firstOrNull { it.restorationIdentifier == to.fullRoute }
        if (prevViewController == null) {
            println("Storyboard: goBack: prevViewController not found")
            return
        }
        navigationController.popToViewController(
            prevViewController, true
        )
    }

    override fun showToastMessage(message: String) {
        println("showErrorMessage: message: $message")
    }
}