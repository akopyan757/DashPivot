package com.cheesecake.common.auth.api

import com.cheesecake.common.api.VersionInfo.SERVER_VERSION

enum class EndPoint(val path: String) {
    REGISTER("/api/$SERVER_VERSION/register"),
    LOGIN("/api/$SERVER_VERSION/login"),
    CONFIRM_EMAIL_BY_CODE("/api/$SERVER_VERSION/verify-code"),
}