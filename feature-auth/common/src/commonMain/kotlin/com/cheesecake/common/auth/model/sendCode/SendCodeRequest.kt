package com.cheesecake.common.auth.model.sendCode

import kotlinx.serialization.Serializable

@Serializable
data class SendCodeRequest(
    val email: String,
    val requestCodeType: SendCodeType
)
