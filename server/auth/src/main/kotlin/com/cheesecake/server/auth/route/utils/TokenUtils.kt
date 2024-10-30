package com.cheesecake.server.auth.route.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

internal object TokenUtils {
    private val SECRET_KEY: String by lazy {
        System.getenv("JWT_SECRET_KEY")
            ?: throw IllegalStateException("SECRET_KEY environment variable not found")
    }
    private const val ISSUER = "dashpivot_server"
    private const val EXPIRATION_TIME_MS = 24 * 60 * 60 * 1000

    fun generateToken(userId: String): String {
        val algorithm = Algorithm.HMAC256(SECRET_KEY)

        return JWT.create()
            .withIssuer(ISSUER)
            .withSubject(userId)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
            .sign(algorithm)
    }
}