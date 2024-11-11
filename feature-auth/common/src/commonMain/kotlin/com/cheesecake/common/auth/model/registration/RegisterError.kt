package com.cheesecake.common.auth.model.registration

import com.cheesecake.common.api.ApiError
import com.cheesecake.common.auth.config.Config
import com.cheesecake.common.auth.utils.PASSWORD_RULES

enum class RegisterError(override val message: String): ApiError {
    EMPTY_EMAIL_ERROR("Email cannot be empty."),
    EMPTY_PASSWORD_ERROR("Password cannot be empty."),
    EMAIL_TAKEN("Email is already taken"),
    INVALID_EMAIL_FORMAT("Invalid email format"),
    INVALID_PASSWORD(PASSWORD_RULES),
    TOO_MANY_REQUESTS("Please wait ${Config.VERIFICATION_CODE_SENDING_DELAY_SEC} seconds since the last registration request."),
    PASSWORD_MATCH("Passwords do not match"),
    VERIFICATION_LETTER_SENDING_ERROR("Error sending verification letter"),
    TOKEN_MISSING("Token is missing"),
    UNKNOWN("Unknown error");

    companion object {
        fun fromMessage(message: String): RegisterError {
            return entries.find { it.message == message } ?: UNKNOWN
        }
    }
}