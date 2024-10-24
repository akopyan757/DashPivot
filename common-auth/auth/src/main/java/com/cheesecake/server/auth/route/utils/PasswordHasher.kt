package com.cheesecake.server.auth.route.utils

import org.mindrot.jbcrypt.BCrypt

object PasswordHasher {
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}