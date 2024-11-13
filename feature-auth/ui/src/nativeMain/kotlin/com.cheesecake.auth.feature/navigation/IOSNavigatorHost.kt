package com.cheesecake.auth.feature.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.cheesecake.auth.feature.di.NativeKoinComponent
import com.cheesecake.common.ui.IOSNavigator
import com.cheesecake.common.ui.events.EventStateHolder
import com.cheesecake.common.ui.navigator.DialogScreen
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
    private val eventStateHolder: EventStateHolder,
) : NavigatorHost {

    private val _regularScreen = MutableStateFlow<RegularScreen>(AuthScreen.Login)
    private val regularScreen: StateFlow<RegularScreen> = _regularScreen

    override val navigator = IOSNavigator(navigationController, eventStateHolder)

    private val navigationDelegate = object : NSObject(), UINavigationControllerDelegateProtocol {

        override fun navigationController(
            navigationController: UINavigationController,
            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") willShowViewController: UIViewController,
            animated: Boolean
        ) {
            val route = willShowViewController.restorationIdentifier
            if (route != null && route.startsWith(RegularScreen.PREFIX)) {
                AuthScreen.regularScreenOfRoute(route)?.let { screen ->
                    _regularScreen.value = if (screen is AuthScreen.Verification) {
                        AuthScreen.Verification { key ->
                            eventStateHolder.getAndRemove(key)
                        }
                    } else {
                        screen
                    }
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

        Box(modifier = Modifier.fillMaxSize()) {
            getComposable(regularScreen, navigator, nativeKoinComponent)
            dialogScreen?.let {
                getComposable(it, navigator, nativeKoinComponent)
            }
        }
    }
}