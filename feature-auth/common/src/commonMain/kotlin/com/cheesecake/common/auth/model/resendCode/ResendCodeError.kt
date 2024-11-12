package com.cheesecake.common.auth.model.resendCode

import com.cheesecake.common.api.ApiError
import com.cheesecake.common.auth.config.Config

enum class ResendCodeError(override val message: String): ApiError {
    USER_NOT_FOUND("User not found for resend code"),
    EMAIL_ALREADY_VERIFIED("Email already verified"),
    TOO_MANY_REQUESTS("Please wait ${Config.VERIFICATION_CODE_SENDING_DELAY_SEC.toInt()} seconds since the last registration request."),
    EMAIL_SENDING_FAILED("Email sending failed"),
}