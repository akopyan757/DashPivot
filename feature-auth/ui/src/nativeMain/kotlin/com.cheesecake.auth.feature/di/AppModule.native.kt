package com.cheesecake.auth.feature.di

import com.cheesecake.auth.feature.events.VerifyEmailEvent
import com.cheesecake.auth.feature.login.LoginViewModel
import com.cheesecake.auth.feature.navigation.IOSNavigatorHost
import com.cheesecake.auth.feature.verification.VerificationViewModel
import com.cheesecake.auth.feature.registration.SignUpViewModel
import com.cheesecake.common.ui.events.EventStateHolder
import com.cheesecake.common.ui.navigator.NavigatorHost
import com.cheesecake.common.ui.navigator.state.DefaultStateManager
import com.cheesecake.common.ui.navigator.state.IStateManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.UIKit.UINavigationController

fun appModule(navigationController: UINavigationController): Module = module {
    single { DefaultStateManager() }
    single { SignUpViewModel(get(), get()) }
    single { LoginViewModel(get()) }
    single { VerificationViewModel(get(), get()) }
    single<NavigatorHost> { IOSNavigatorHost(navigationController, get()) }
    single { EventStateHolder<VerifyEmailEvent>() }
}

class NativeKoinComponent: AppKoinComponent, KoinComponent {
    override fun getVerificationViewModel(): VerificationViewModel = get()
    override fun getSignUpViewModel(): SignUpViewModel = get()
    override fun getLoginViewModel(): LoginViewModel = get()
    override fun getEventStateHolder(): EventStateHolder<VerifyEmailEvent> = get()
    override fun getStateManager(): IStateManager = get()
    fun getNavigatorHost(): NavigatorHost = get()
}