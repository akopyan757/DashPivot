package com.cheesecake.common.auth.model.registration

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val email: String
)
