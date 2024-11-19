package com.cheesecake.auth.feature.di

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import com.cheesecake.auth.feature.login.LoginState
import com.cheesecake.auth.feature.login.LoginViewModel
import com.cheesecake.auth.feature.navigation.AndroidNavigatorHost
import com.cheesecake.auth.feature.navigation.AuthSerializers
import com.cheesecake.auth.feature.registration.SignUpViewModel
import com.cheesecake.auth.feature.verification.VerificationViewModel
import com.cheesecake.common.ui.AndroidNavigator
import com.cheesecake.common.ui.events.EventStateHolder
import com.cheesecake.common.ui.navigator.Navigator
import com.cheesecake.common.ui.navigator.NavigatorHost
import com.cheesecake.common.ui.navigator.state.AndroidStateManager
import com.cheesecake.common.ui.navigator.state.IKClassSerializers
import com.cheesecake.common.ui.navigator.state.IStateManager
import com.cheesecake.common.ui.state.UIStateManagerImpl
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
        scoped<IStateManager> { AndroidStateManager(get(), get()) }
        viewModel { SignUpViewModel(get(), get()) }
        viewModel {
            val stateStrategy = UIStateManagerImpl(get(), LoginState.KEY, LoginState.serializer())
            LoginViewModel(get(), stateStrategy)
        }
        viewModel { VerificationViewModel(get(), get(), get()) }
    }

    single<EventStateHolder> { EventStateHolder() }
    single<IKClassSerializers> { AuthSerializers() }
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
    override fun getStateManager(): IStateManager = getAuthScope().get()
}