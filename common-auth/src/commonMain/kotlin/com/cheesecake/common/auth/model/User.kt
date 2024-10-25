package com.cheesecake.common.auth.model

data class User(
    val id: Int,
    val email: String,
    val passwordHash: String,
    val isVerified: Boolean,
)