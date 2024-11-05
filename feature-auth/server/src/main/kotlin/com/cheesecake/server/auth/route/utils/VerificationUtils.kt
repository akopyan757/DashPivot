package com.cheesecake.server.auth.route.utils

import java.util.UUID
import kotlin.math.pow

object VerificationUtils {
    fun generateVerificationToken(): String {
        return UUID.randomUUID().toString()
    }

    fun generateVerificationCode(digitsCount: Int): String {
        val max = 10.0.pow(digitsCount.toDouble()).toInt() - 1
        val min = 10.0.pow((digitsCount - 1).toDouble()).toInt()
        return (min..max).random().toString().also {
            println("min: $min, max: $max, code: $it")
        }
    }
}