package com.cheesecake.common.auth.model

import com.cheesecake.common.api.ApiError
import com.cheesecake.common.auth.utils.PASSWORD_RULES

enum class RegisterError(override val message: String): ApiError {
    EMPTY_EMAIL_ERROR("Email cannot be empty."),
    EMPTY_PASSWORD_ERROR("Password cannot be empty."),
    EMAIL_TAKEN("Email is already taken"),
    INVALID_EMAIL_FORMAT("Invalid email format"),
    INVALID_PASSWORD(PASSWORD_RULES),
    PASSWORD_MATCH("Passwords do not match"),
    TOKEN_MISSING("Token is missing"),
    UNKNOWN("Unknown error");

    companion object {
        fun fromMessage(message: String): RegisterError {
            return entries.find { it.message == message } ?: UNKNOWN
        }
    }
}