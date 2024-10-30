package com.cheesecake.auth.feature.di

import com.cheesecake.auth.feature.login.LoginViewModel
import com.cheesecake.auth.feature.verification.VerificationViewModel
import com.cheesecake.auth.feature.registration.SignUpViewModel
import org.koin.dsl.module

actual val appModule = module {
    single { SignUpViewModel(get()) }
    single { LoginViewModel(get()) }
    single { VerificationViewModel(get()) }
}