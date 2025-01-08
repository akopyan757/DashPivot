package com.cheesecake.common.auth.model.changePassword

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val email: String,
    val code: String,
    val newHashedPassword: String,
)