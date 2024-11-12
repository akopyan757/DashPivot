package com.cheesecake.server.auth.route.utils

import org.mindrot.jbcrypt.BCrypt

interface IPasswordHasher {
    fun hashPassword(password: String): String
    fun verifyPassword(password: String, hashedPassword: String): Boolean
}

internal class PasswordHasher: IPasswordHasher {
    override fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    override fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }
}