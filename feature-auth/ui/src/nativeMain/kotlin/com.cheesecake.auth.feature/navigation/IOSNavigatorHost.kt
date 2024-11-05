package com.cheesecake.auth.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cheesecake.auth.feature.di.NativeKoinComponent
import com.cheesecake.auth.feature.events.VerifyEmailEvent
import com.cheesecake.common.ui.IOSNavigator
import com.cheesecake.common.ui.events.EventStateHolder
import com.cheesecake.common.ui.navigator.DialogScreen
import com.cheesecake.common.ui.navigator.Navigator
import com.cheesecake.common.ui.navigator.NavigatorHost
import com.cheesecake.common.ui.navigator.RegularScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.UIKit.UINavigationController
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.UIKit.restorationIdentifier
import platform.darwin.NSObject

class IOSNavigatorHost(
    navigationController: UINavigationController,
    private val eventStateHolder: EventStateHolder<VerifyEmailEvent>,
) : NavigatorHost {

    private val _regularScreen = MutableStateFlow<RegularScreen>(AuthScreen.Login)
    private val regularScreen: StateFlow<RegularScreen> = _regularScreen

    override val navigator: Navigator = IOSNavigator(navigationController)

    private val navigationDelegate = object : NSObject(), UINavigationControllerDelegateProtocol {
        override fun navigationController(
            navigationController: UINavigationController,
            willShowViewController: UIViewController,
            animated: Boolean
        ) {
            val route = willShowViewController.restorationIdentifier
            if (route != null && route.startsWith(RegularScreen.PREFIX)) {
                AuthScreen.regularScreenOfRoute(route)?.let {
                    _regularScreen.value = it
                }
            }
        }
    }

    init {
        navigationController.delegate = navigationDelegate
    }

    @Composable
    override fun Screen() {
        val nativeKoinComponent = NativeKoinComponent()
        val dialogScreen: DialogScreen? by navigator.currentDialog.collectAsState()
        val regularScreen: RegularScreen by regularScreen.collectAsState()

        getComposable(regularScreen, navigator, nativeKoinComponent)
        dialogScreen?.let {
            getComposable(it, navigator, nativeKoinComponent)
        }
    }
}