package com.cheesecake.common.ui

import com.cheesecake.common.ui.events.EventStateHolder
import com.cheesecake.common.ui.navigator.DialogScreen
import com.cheesecake.common.ui.navigator.Navigator
import com.cheesecake.common.ui.navigator.RegularScreen
import com.cheesecake.common.ui.toast.ToastMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.UIKit.UINavigationController
import platform.UIKit.UIStoryboard
import platform.UIKit.UIViewController
import platform.UIKit.restorationIdentifier

class IOSNavigator(
    private val navigationController: UINavigationController,
    private val eventStateHolder: EventStateHolder,
): Navigator {

    private val _currentDialog = MutableStateFlow<DialogScreen?>(null)
    override val currentDialog: StateFlow<DialogScreen?> = _currentDialog

    private val _toastMessage = MutableStateFlow<ToastMessage>(ToastMessage.Idle)
    override val toastMessage: StateFlow<ToastMessage> = _toastMessage

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



    override fun showToastMessage(toast: ToastMessage) {
        _toastMessage.value = toast
    }

    override fun dismissToastMessage() {
        _toastMessage.value = ToastMessage.Idle
    }
}