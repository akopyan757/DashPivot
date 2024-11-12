package com.cheesecake.server.auth.route.utils

import kotlin.math.pow

interface IVerifyCodeGenerator {
    fun generateVerificationCode(digitsCount: Int): String
}

internal class VerifyCodeGenerator: IVerifyCodeGenerator {
    override fun generateVerificationCode(digitsCount: Int): String {
        val max = 10.0.pow(digitsCount.toDouble()).toInt() - 1
        val min = 10.0.pow((digitsCount - 1).toDouble()).toInt()
        return (min..max).random().toString()
    }
}