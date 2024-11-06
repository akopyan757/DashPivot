package com.cheesecake.auth.feature.di

import com.cheesecake.auth.feature.events.VerifyEmailEvent
import com.cheesecake.auth.feature.login.LoginViewModel
import com.cheesecake.auth.feature.registration.SignUpViewModel
import com.cheesecake.auth.feature.verification.VerificationViewModel
import com.cheesecake.common.ui.events.EventStateHolder
import com.cheesecake.common.ui.navigator.state.IStateManager
import org.koin.core.component.KoinComponent

interface AppKoinComponent: KoinComponent {
    fun getVerificationViewModel(): VerificationViewModel
    fun getSignUpViewModel(): SignUpViewModel
    fun getLoginViewModel(): LoginViewModel
    fun getEventStateHolder(): EventStateHolder<VerifyEmailEvent>
    fun getStateManager(): IStateManager
}