package com.cheesecake.common.auth.model.verefication

import com.cheesecake.common.api.ApiError

enum class VerificationError(override val message: String): ApiError {
    USER_NOT_FOUND("User not found"),
    VERIFICATION_CODE_NOT_FOUND("Verification code not found"),
    EMPTY_CODE_ERROR("Code cannot be empty."),
    EXPIRED_CODE("Invalid or expired code"),
    UNKNOWN("Unknown error");

    companion object {
        fun fromMessage(message: String): VerificationError {
            return entries.find { it.message == message } ?: UNKNOWN
        }
    }
}