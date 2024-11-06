package com.cheesecake.auth.feature.navigation

import com.cheesecake.auth.feature.state.VerificationResendTimer
import com.cheesecake.common.ui.navigator.state.IKClassSerializers
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

class AuthSerializers: IKClassSerializers {
    override val serializers: Map<KClass<*>, KSerializer<*>> = mapOf(
        VerificationResendTimer::class to VerificationResendTimer.serializer(),
    )
}