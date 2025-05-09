package com.cheesecake.common.auth.model.error

import com.cheesecake.common.api.ApiError
import io.ktor.http.HttpStatusCode
enum class AuthError(
    override val code: Int,
    override val message: String
) : ApiError {

    // Common Error
    UNKNOWN(HttpStatusCode.InternalServerError),
    NETWORK_ERROR(HttpStatusCode.InternalServerError.value, "Network error"),
    TOO_MANY_REQUESTS(HttpStatusCode.TooManyRequests),
    UNAUTHORIZED(HttpStatusCode.Unauthorized),
    NOT_FOUND(HttpStatusCode.NotFound),

    // Login and registration Error
    EMPTY_EMAIL_ERROR(HttpStatusCode.BadRequest.value, "Email cannot be empty."),
    EMPTY_PASSWORD_ERROR(HttpStatusCode.BadRequest.value, "Password cannot be empty."),
    INVALID_PASSWORD(HttpStatusCode.Unauthorized.value, "Invalid password"),
    EMAIL_NOT_VERIFIED(HttpStatusCode.Forbidden.value, "Email not verified"),
    INVALID_EMAIL_FORMAT(HttpStatusCode.BadRequest.value, "Invalid email format"),

    // Registration Error
    EMAIL_TAKEN(HttpStatusCode.Conflict.value, "Email is already taken"),
    PASSWORD_MATCH(HttpStatusCode.BadRequest.value, "Passwords do not match"),
    VERIFICATION_LETTER_SENDING_ERROR(HttpStatusCode.InternalServerError.value, "Error sending verification letter"),
    TOKEN_MISSING(HttpStatusCode.BadRequest.value, "Token is missing"),

    // Send error code
    USER_NOT_VERIFIED(HttpStatusCode.Conflict.value, "User not verified"),
    EMAIL_ALREADY_VERIFIED(HttpStatusCode.Conflict.value, "Email already verified"),
    EMAIL_SENDING_FAILED(HttpStatusCode.InternalServerError.value,"Email sending failed"),

    // Verification Error
    VERIFICATION_CODE_NOT_FOUND(HttpStatusCode.NotFound.value, "Verification code not found"),
    EMPTY_CODE_ERROR(HttpStatusCode.BadRequest.value, "Code cannot be empty."),
    EXPIRED_CODE(HttpStatusCode.Unauthorized.value, "Invalid or expired code"),

    // Change password Error
    USER_NOT_FOUND(HttpStatusCode.NotFound.value, "User not found"),
    SAME_PASSWORD(HttpStatusCode.BadRequest.value,"Password cannot be the same as the old one");

    constructor(httpStatusCode: HttpStatusCode) : this(
        httpStatusCode.value,
        httpStatusCode.description
    )
}