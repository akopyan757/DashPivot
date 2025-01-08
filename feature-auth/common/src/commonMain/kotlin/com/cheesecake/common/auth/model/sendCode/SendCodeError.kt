package com.cheesecake.common.auth.model.sendCode

import com.cheesecake.common.api.ApiError
import com.cheesecake.common.auth.config.Config

enum class SendCodeError(override val message: String): ApiError {
    USER_NOT_FOUND("User not found for resend code"),
    USER_NOT_VERIFIED("User not verified"),
    EMAIL_ALREADY_VERIFIED("Email already verified"),
    TOO_MANY_REQUESTS("Please wait ${Config.VERIFICATION_CODE_SENDING_DELAY_SEC.toInt()} seconds since the last registration request."),
    EMAIL_SENDING_FAILED("Email sending failed"),
    UNKNOWN("Unknown error");

    companion object {
        fun fromMessage(message: String): SendCodeError {
            return SendCodeError.entries.find { it.message == message } ?: UNKNOWN
        }
    }
}