package com.cheesecake.auth.feature.di

import com.cheesecake.auth.feature.login.LoginState
import com.cheesecake.auth.feature.login.LoginViewModel
import com.cheesecake.auth.feature.navigation.IOSNavigatorHost
import com.cheesecake.auth.feature.registration.SignUpState
import com.cheesecake.auth.feature.registration.SignUpViewModel
import com.cheesecake.auth.feature.verification.VerificationState
import com.cheesecake.auth.feature.verification.VerificationViewModel
import com.cheesecake.common.ui.events.EventStateHolder
import com.cheesecake.common.ui.navigator.NavigatorHost
import com.cheesecake.common.ui.state.UIStateManager
import com.cheesecake.common.ui.state.cache.DefaultStateCache
import com.cheesecake.common.ui.state.cache.StateCache
import com.cheesecake.common.ui.state.UIStateManagerImpl
import com.cheesecake.common.ui.state.UIStateManagerWithoutCacheImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.UIKit.UINavigationController

fun appModule(navigationController: UINavigationController): Module = module {
    single<StateCache> { DefaultStateCache() }
    single<UIStateManager<LoginState>>(named(LoginState.KEY)) {
        UIStateManagerImpl(get(), LoginState.KEY, LoginState.serializer())
    }
    single<UIStateManager<SignUpState>>(named(SignUpState.KEY)) {
        UIStateManagerImpl(get(), SignUpState.KEY, SignUpState.serializer())
    }
    single<UIStateManager<VerificationState>>(named(VerificationState.KEY)) {
        UIStateManagerWithoutCacheImpl(VerificationState.serializer())
    }
    single { SignUpViewModel(get(), get(named(SignUpState.KEY))) }
    single { LoginViewModel(get(), get(named(LoginState.KEY))) }
    single {
        VerificationViewModel(
            get(), get(),
            get(named(VerificationState.KEY)),
            get(named(LoginState.KEY)),
            get(named(SignUpState.KEY)),
        )
    }
    single<NavigatorHost> { IOSNavigatorHost(navigationController, get()) }
    single { EventStateHolder() }
}

class NativeKoinComponent: AppKoinComponent, KoinComponent {
    override fun getVerificationViewModel(): VerificationViewModel = get()
    override fun getSignUpViewModel(): SignUpViewModel = get()
    override fun getLoginViewModel(): LoginViewModel = get()
    override fun getEventStateHolder(): EventStateHolder = get()
    override fun getStateManager(): StateCache = get()
    fun getNavigatorHost(): NavigatorHost = get()
}