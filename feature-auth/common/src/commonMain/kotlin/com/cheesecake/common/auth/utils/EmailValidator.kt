package com.cheesecake.common.auth.utils

private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+(\\.[A-Za-z0-9+_.-]+)*@[A-Za-z0-9.-]+(\\.[A-Za-z0-9]+)+$"

fun isValidEmail(email: String): Boolean {
    val domainPart = email.substringAfterLast(".")
    if (domainPart.length < 2) return false
    if (email.contains("..")) return false

    return EMAIL_REGEX.toRegex().matches(email)
}