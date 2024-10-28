package com.cheesecake.common.auth.api

import com.cheesecake.common.api.VERSION

enum class EndPoint(val path: String) {
    REGISTER("/api/$VERSION/register"),
    CONFIRM_EMAIL("/$VERSION/confirm-email");
}