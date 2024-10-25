package com.cheesecake.common.auth.model

import com.cheesecake.common.api.ApiError

enum class RegisterError(override val message: String): ApiError {
    EMAIL_TAKEN("Email is already taken"),
    INVALID_EMAIL_FORMAT("Invalid email format"),
    INVALID_PASSWORD("Password does not meet the requirements"),
    TOKEN_MISSING("Token is missing"),
    EXPIRED_TOKEN("Invalid or expired token"),
    UNKNOWN("Unknown error");

    companion object {
        fun fromMessage(message: String): RegisterError {
            return entries.find { it.message == message } ?: UNKNOWN
        }
    }
}