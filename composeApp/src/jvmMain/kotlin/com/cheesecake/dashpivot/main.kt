package com.cheesecake.dashpivot

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cheesecake.auth.data.di.authDataModule
import com.cheesecake.auth.feature.di.appModule
import com.cheesecake.auth.feature.domain.di.authDomainModule
import org.koin.core.context.GlobalContext.startKoin

fun main() = application {
    startKoin {
        modules(appModule, authDomainModule, authDataModule)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "DashPivot",
    ) {
        App()
    }
}