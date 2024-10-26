package com.cheesecake.auth.feature.di

import com.cheesecake.auth.feature.viewmodel.SignUpViewModel
import org.koin.dsl.module

actual val appModule = module {
    single { SignUpViewModel(get()) }
}