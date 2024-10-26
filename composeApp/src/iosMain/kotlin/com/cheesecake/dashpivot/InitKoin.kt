package com.cheesecake.dashpivot

import com.cheesecake.auth.data.di.authDataModule
import com.cheesecake.auth.feature.di.appModule
import org.koin.core.context.startKoin

fun InitKoin() {
    startKoin {
        modules(appModule, authDataModule)
    }
}