package com.cheesecake.auth.feature.di

import com.cheesecake.auth.feature.login.LoginViewModel
import com.cheesecake.auth.feature.verification.VerificationViewModel
import com.cheesecake.auth.feature.registration.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

actual val appModule: Module = module {
    viewModel { SignUpViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { VerificationViewModel(get()) }
}