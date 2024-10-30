package com.cheesecake.common.auth.api

import com.cheesecake.common.api.VersionInfo.SERVER_VERSION

enum class EndPoint(val path: String) {
    REGISTER("/api/$SERVER_VERSION/register"),
    CONFIRM_EMAIL("/$SERVER_VERSION/confirm-email"),
    LOGIN("/api/$SERVER_VERSION/login");
}