package com.cheesecake.common.auth.api

enum class EndPoint(val path: String) {
    REGISTER("/api/register"),
    CONFIRM_EMAIL("/api/confirm-email");
}