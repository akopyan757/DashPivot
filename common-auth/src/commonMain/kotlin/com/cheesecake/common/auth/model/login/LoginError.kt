package com.cheesecake.common.auth.model.login

import com.cheesecake.common.api.ApiError
import com.cheesecake.common.auth.utils.PASSWORD_RULES

enum class LoginError(override val message: String): ApiError {
    USER_NOT_FOUND("User with this email was not found."),
    INVALID_PASSWORD("The password entered is incorrect."),
    EMAIL_NOT_VERIFIED("Email has not been verified. Please verify your email to proceed."),
    UNKNOWN("An unknown error occurred.");

    companion object {
        fun fromMessage(message: String): LoginError {
            return entries.find { it.message == message } ?: UNKNOWN
        }
    }
}