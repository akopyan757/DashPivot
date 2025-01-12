package com.cheesecake.common.auth.api

import com.cheesecake.common.api.VersionInfo.SERVER_VERSION

enum class EndPoint(val path: String) {
    REGISTER("/api/$SERVER_VERSION/register"),
    REGISTER_CONFIRM("/api/$SERVER_VERSION/register-confirm"),
    REGISTER_RESEND_CODE("/api/$SERVER_VERSION/register-resend-code"),

    RESET_PASSWORD_SEND_CODE("/api/$SERVER_VERSION/reset-password-send-code"),
    RESET_PASSWORD("/api/$SERVER_VERSION/reset-password"),

    LOGIN("/api/$SERVER_VERSION/login"),
}