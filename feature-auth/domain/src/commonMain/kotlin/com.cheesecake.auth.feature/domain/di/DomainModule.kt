package com.cheesecake.auth.feature.domain.di

import com.cheesecake.auth.feature.domain.usecase.LoginUseCase
import com.cheesecake.auth.feature.domain.usecase.RegisterUseCase
import com.cheesecake.auth.feature.domain.usecase.ResendVerificationRegisterCodeUseCase
import com.cheesecake.auth.feature.domain.usecase.VerificationUseCase

val authDomainModule = org.koin.dsl.module {
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { VerificationUseCase(get()) }
    factory { ResendVerificationRegisterCodeUseCase(get()) }
}