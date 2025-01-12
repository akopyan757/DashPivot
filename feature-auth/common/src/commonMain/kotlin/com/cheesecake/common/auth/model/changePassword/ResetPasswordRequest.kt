package com.cheesecake.common.auth.model.changePassword

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    @SerialName("email") val email: String,
    @SerialName("code") val code: String,
    @SerialName("password") val password: String,
)