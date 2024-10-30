package com.cheesecake.auth.feature.di

import com.cheesecake.auth.feature.verification.VerificationViewModel
import com.cheesecake.auth.feature.registration.SignUpViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

actual val appModule: Module = module {
    single { SignUpViewModel(get()) }
    single { VerificationViewModel(get()) }
}