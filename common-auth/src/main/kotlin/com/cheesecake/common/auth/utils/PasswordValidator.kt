package com.cheesecake.common.auth.utils

private const val PASSWORD_MIN_LIMIT = 8
private const val PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{$PASSWORD_MIN_LIMIT,}$"

fun isValidPassword(password: String): Boolean {
    return PASSWORD_REGEX.toRegex().matches(password)
}