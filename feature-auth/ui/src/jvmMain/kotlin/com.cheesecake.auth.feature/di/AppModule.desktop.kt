package com.cheesecake.auth.feature.di

import com.cheesecake.auth.feature.login.LoginViewModel
import com.cheesecake.auth.feature.navigation.DesktopNavigatorHost
import com.cheesecake.auth.feature.registration.SignUpViewModel
import com.cheesecake.auth.feature.verification.VerificationViewModel
import com.cheesecake.common.ui.events.EventStateHolder
import com.cheesecake.common.ui.navigator.NavigatorHost
import com.cheesecake.common.ui.navigator.state.DefaultStateManager
import com.cheesecake.common.ui.navigator.state.IStateManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.dsl.module

val appModule = module {
    single { DefaultStateManager() }
    single { SignUpViewModel(get(), get()) }
    single { LoginViewModel(get()) }
    single { VerificationViewModel(get(), get()) }
    single<NavigatorHost> { DesktopNavigatorHost() }
    single { EventStateHolder() }
    single<IStateManager> { DefaultStateManager() }
}

class JvmKoinComponent : AppKoinComponent, KoinComponent {
    override fun getVerificationViewModel(): VerificationViewModel = get()
    override fun getSignUpViewModel(): SignUpViewModel = get()
    override fun getLoginViewModel(): LoginViewModel = get()
    override fun getEventStateHolder(): EventStateHolder = get()
    override fun getStateManager(): IStateManager = get()
    fun getNavigatorHost(): NavigatorHost = get()
}
