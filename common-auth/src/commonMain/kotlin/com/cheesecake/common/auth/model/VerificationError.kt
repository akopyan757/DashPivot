package com.cheesecake.common.auth.model

import com.cheesecake.common.api.ApiError

enum class VerificationError(override val message: String): ApiError {
    EMPTY_TOKEN_ERROR("Token cannot be empty."),
    EXPIRED_TOKEN("Invalid or expired token"),
    UNKNOWN("Unknown error");

    companion object {
        fun fromMessage(message: String): VerificationError {
            return entries.find { it.message == message } ?: UNKNOWN
        }
    }
}