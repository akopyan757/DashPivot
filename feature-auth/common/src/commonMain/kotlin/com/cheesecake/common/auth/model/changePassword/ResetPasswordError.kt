package com.cheesecake.common.auth.model.changePassword

import com.cheesecake.common.api.ApiError

enum class ResetPasswordError(override val message: String): ApiError {
    USER_NOT_FOUND("User not found"),
    USER_NOT_VERIFIED("User not verified"),
    VERIFICATION_CODE_NOT_FOUND("Verification code not found"),
    SAME_PASSWORD("Password cannot be the same as the old one"),
    EXPIRED_CODE("Invalid or expired code"),
    UNKNOWN("Unknown error");

    companion object {
        fun fromMessage(message: String): ResetPasswordError {
            return entries.find { it.message == message } ?: UNKNOWN
        }
    }
}