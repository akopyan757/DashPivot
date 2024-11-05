package com.cheesecake.common.auth.model.verefication

import com.cheesecake.common.api.ApiError

enum class VerificationError(override val message: String): ApiError {
    EMPTY_TOKEN_ERROR("Token cannot be empty."),
    EXPIRED_TOKEN("Invalid or expired token"),
    EMPTY_CODE_ERROR("Code cannot be empty."),
    EXPIRED_CODE("Invalid or expired code"),
    UNKNOWN("Unknown error");

    companion object {
        fun fromMessage(message: String): VerificationError {
            return entries.find { it.message == message } ?: UNKNOWN
        }
    }
}