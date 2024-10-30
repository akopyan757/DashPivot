package com.cheesecake.dashpivot

import com.cheesecake.auth.feature.di.NativeKoinComponent
import com.cheesecake.auth.feature.events.VerifyEmailEvent
import kotlinx.coroutines.runBlocking

fun tokenUpdate(token: String?) = runBlocking {
    token?.let {
        NativeKoinComponent().getEventStateHolder().emitEvent(VerifyEmailEvent(token))
    }
}