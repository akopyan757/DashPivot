package com.cheesecake.common.auth.utils

private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"

fun isValidEmail(email: String): Boolean {
    // Проверка на отсутствие двух точек подряд и на наличие символов после точки
    if (email.contains("..")) return false
    // Проверка, чтобы после последней точки были хотя бы 2 символа
    val domainPart = email.substringAfterLast(".")
    if (domainPart.length < 2) return false

    return EMAIL_REGEX.toRegex().matches(email)
}