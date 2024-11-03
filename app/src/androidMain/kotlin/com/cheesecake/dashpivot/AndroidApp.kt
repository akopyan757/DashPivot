package com.cheesecake.dashpivot

import android.app.Application
import com.cheesecake.auth.data.di.authDataModule
import com.cheesecake.auth.feature.di.authAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AndroidApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AndroidApp)
            modules(authDataModule, authAppModule)
        }
    }
}