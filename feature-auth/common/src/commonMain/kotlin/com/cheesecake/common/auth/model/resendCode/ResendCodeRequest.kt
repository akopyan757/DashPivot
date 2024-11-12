package com.cheesecake.common.auth.model.resendCode

import kotlinx.serialization.Serializable

@Serializable
data class ResendCodeRequest(
    val email: String
)
