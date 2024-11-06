package com.cheesecake.common.ui.navigator.state

import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

interface IKClassSerializers {
    val serializers: Map<KClass<*>, KSerializer<*>>
}