package com.cheesecake.common.auth.model.login

import com.cheesecake.common.api.ApiError

enum class LoginError(override val message: String): ApiError {
    EMPTY_EMAIL_ERROR("Email cannot be empty."),
    EMPTY_PASSWORD_ERROR("Password cannot be empty."),
    USER_NOT_FOUND("User not found"),
    INVALID_PASSWORD("Invalid password"),
    EMAIL_NOT_VERIFIED("Email not verified"),
    INVALID_EMAIL_FORMAT("Invalid email format"),
    UNKNOWN("Unknown error");

    companion object {
        fun fromMessage(message: String): LoginError {
            return entries.find { it.message == message } ?: UNKNOWN
        }
    }
}