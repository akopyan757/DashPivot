package com.cheesecake.auth.feature.events

import com.cheesecake.common.ui.events.EventState

data class VerifyEmailEvent(val email: String): EventState