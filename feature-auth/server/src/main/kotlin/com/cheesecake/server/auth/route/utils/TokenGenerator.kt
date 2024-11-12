package com.cheesecake.server.auth.route.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

interface ITokenGenerator {
    fun generateToken(id: String): String
}

internal object TokenGenerator : ITokenGenerator {
    private val SECRET_KEY: String by lazy {
        System.getenv("JWT_SECRET_KEY")
            ?: throw IllegalStateException("SECRET_KEY environment variable not found")
    }
    private const val ISSUER = "dashpivot_server"
    private const val EXPIRATION_TIME_MS = 24 * 60 * 60 * 1000

    override fun generateToken(id: String): String {
        val algorithm = Algorithm.HMAC256(SECRET_KEY)

        return JWT.create()
            .withIssuer(ISSUER)
            .withSubject(id)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
            .sign(algorithm)
    }
}