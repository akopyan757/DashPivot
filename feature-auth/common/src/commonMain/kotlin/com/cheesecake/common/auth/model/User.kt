package com.cheesecake.common.auth.model

data class User(
    val id: Int,
    val email: String,
    val passwordHash: String,
    val isVerified: Boolean,
)

data class UserVerify(
    val id: Int,
    val email: String,
    val passwordHash: String?,
    val verificationHashedCode: String?,
    val createdAt: String?,
)