package com.cheesecake.auth.feature

import com.cheesecake.auth.data.di.authDataModule
import com.cheesecake.auth.feature.di.appModule
import com.cheesecake.auth.feature.domain.di.authDomainModule
import org.koin.core.context.startKoin
import platform.UIKit.UINavigationController

fun AuthInitKoin(viewController: UINavigationController) {
    startKoin {
        modules(appModule(viewController), authDomainModule, authDataModule)
    }
}