package com.cheesecake.common.auth.model.verefication

import kotlinx.serialization.Serializable

@Serializable
data class VerificationRequest(
    val email: String,
    val code: String
)
