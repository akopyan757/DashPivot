package com.cheesecake.auth.feature.di

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import com.cheesecake.auth.feature.login.LoginState
import com.cheesecake.auth.feature.login.LoginViewModel
import com.cheesecake.auth.feature.navigation.AndroidNavigatorHost
import com.cheesecake.auth.feature.registration.SignUpState
import com.cheesecake.auth.feature.registration.SignUpViewModel
import com.cheesecake.auth.feature.verification.VerificationState
import com.cheesecake.auth.feature.verification.VerificationViewModel
import com.cheesecake.common.ui.AndroidNavigator
import com.cheesecake.common.ui.events.EventStateHolder
import com.cheesecake.common.ui.navigator.Navigator
import com.cheesecake.common.ui.navigator.NavigatorHost
import com.cheesecake.common.ui.navigator.state.AndroidStateCache
import com.cheesecake.common.ui.state.UIStateManager
import com.cheesecake.common.ui.state.cache.StateCache
import com.cheesecake.common.ui.state.UIStateManagerImpl
import com.cheesecake.common.ui.state.UIStateManagerWithoutCacheImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.get
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module


fun screenModule(navController: NavHostController): Module = module {
    scope(named("MainContent")) {
        scoped { navController }
        scoped<Navigator> { AndroidNavigator(get()) }
        scoped<NavigatorHost> { AndroidNavigatorHost(get(), get()) }
        scoped<SavedStateHandle> {
            get<NavHostController>().currentBackStackEntry?.savedStateHandle ?:
                throw IllegalStateException("No current back stack entry")
        }
        scoped<StateCache> {
            AndroidStateCache(get())
        }
        scoped<UIStateManager<SignUpState>>(named(SignUpState.KEY)) {
            UIStateManagerImpl(get(), SignUpState.KEY, SignUpState.serializer())
        }
        scoped<UIStateManager<LoginState>>(named(LoginState.KEY)) {
            UIStateManagerImpl(get(), LoginState.KEY, LoginState.serializer())
        }
        scoped<UIStateManager<VerificationState>>(named(VerificationState.KEY)) {
            UIStateManagerWithoutCacheImpl(VerificationState.serializer())
        }
        viewModel { SignUpViewModel(get(), get(named(SignUpState.KEY))) }
        viewModel { LoginViewModel(get(), get(named(LoginState.KEY))) }
        viewModel {
            VerificationViewModel(
                get(), get(),
                get(named(VerificationState.KEY)),
                get(named(LoginState.KEY)),
                get(named(SignUpState.KEY))
            )
        }
    }

    single<EventStateHolder> { EventStateHolder() }
}

object AndroidKoinComponent : AppKoinComponent, KoinComponent {
    private var _scope: Scope? = null

    fun setAuthScope(scope: Scope) {
        this._scope = scope
    }

    fun releaseScope() {
        _scope?.close()
        _scope = null
    }

    private fun getAuthScope() = _scope ?: throw IllegalStateException("Scope is not initialized")

    override fun getVerificationViewModel(): VerificationViewModel = getAuthScope().get()
    override fun getSignUpViewModel(): SignUpViewModel = getAuthScope().get()
    override fun getLoginViewModel(): LoginViewModel = getAuthScope().get()
    override fun getEventStateHolder(): EventStateHolder = get().get()
    override fun getStateManager(): StateCache = getAuthScope().get()
}