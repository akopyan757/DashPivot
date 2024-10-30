package com.cheesecake.auth.feature.di

import androidx.navigation.NavHostController
import com.cheesecake.auth.feature.events.VerifyEmailEvent
import com.cheesecake.auth.feature.login.LoginViewModel
import com.cheesecake.auth.feature.navigation.AndroidNavigatorHost
import com.cheesecake.auth.feature.verification.VerificationViewModel
import com.cheesecake.auth.feature.registration.SignUpViewModel
import com.cheesecake.common.ui.AndroidNavigator
import com.cheesecake.common.ui.events.EventStateHolder
import com.cheesecake.common.ui.navigator.Navigator
import com.cheesecake.common.ui.navigator.NavigatorHost
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.get
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authAppModule = module {
    single { EventStateHolder<VerifyEmailEvent>() }
}

fun screenModule(navController: NavHostController): Module = module {
    scope(named("MainContent")) {
        scoped { navController }
        scoped<Navigator> { AndroidNavigator(get()) }
        scoped<NavigatorHost> { AndroidNavigatorHost(get(), get(), get()) }
    }
    viewModel { SignUpViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { VerificationViewModel(get()) }
}

class AndroidKoinComponent : AppKoinComponent, KoinComponent {
    override fun getVerificationViewModel(): VerificationViewModel = get().get()
    override fun getSignUpViewModel(): SignUpViewModel = get().get()
    override fun getLoginViewModel(): LoginViewModel = get().get()
    override fun getEventStateHolder(): EventStateHolder<VerifyEmailEvent> = get().get()
}