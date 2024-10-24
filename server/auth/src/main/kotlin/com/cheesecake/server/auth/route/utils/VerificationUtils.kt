package com.cheesecake.server.auth.route.utils

import java.util.UUID

object VerificationUtils {
    fun generateVerificationToken(): String {
        return UUID.randomUUID().toString()
    }
}