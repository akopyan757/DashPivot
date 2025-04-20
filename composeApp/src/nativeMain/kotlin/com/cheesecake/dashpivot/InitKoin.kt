package com.cheesecake.dashpivot

import com.cheesecake.auth.feature.AuthInitKoin
import platform.UIKit.UINavigationController

fun InitKoin(viewController: UINavigationController) {
    AuthInitKoin(viewController)
}