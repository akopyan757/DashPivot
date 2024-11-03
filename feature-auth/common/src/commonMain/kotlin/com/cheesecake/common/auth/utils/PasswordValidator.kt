package com.cheesecake.common.auth.utils

private const val PASSWORD_MIN_LIMIT = 8
private const val PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{$PASSWORD_MIN_LIMIT,}$"
const val PASSWORD_RULES = "Password must have $PASSWORD_MIN_LIMIT+ chars, uppercase, lowercase, number & special char."

private const val PASSWORD_MIN_LENGTH_RULE = "At least $PASSWORD_MIN_LIMIT characters."
private const val PASSWORD_UPPERCASE_RULE = "At least one uppercase letter (A-Z)."
private const val PASSWORD_LOWERCASE_RULE = "At least one lowercase letter (a-z)."
private const val PASSWORD_NUMBER_RULE = "At least one number (0-9)."
private const val PASSWORD_SPECIAL_CHAR_RULE = "At least one special character (@#\$%&*)."

// Separate regex patterns for each rule
private val PASSWORD_MIN_LENGTH_REGEX = ".{$PASSWORD_MIN_LIMIT,}".toRegex()
private val PASSWORD_UPPERCASE_REGEX = ".*[A-Z].*".toRegex()
private val PASSWORD_LOWERCASE_REGEX = ".*[a-z].*".toRegex()
private val PASSWORD_NUMBER_REGEX = ".*\\d.*".toRegex()
private val PASSWORD_SPECIAL_CHAR_REGEX = ".*[@\$!%*?&].*".toRegex()

fun isValidPassword(password: String): Boolean {
    return PASSWORD_REGEX.toRegex().matches(password)
}

fun validatePassword(password: String): List<String> {
    val errors = mutableListOf<String>()

    if (!PASSWORD_MIN_LENGTH_REGEX.matches(password)) {
        errors.add(PASSWORD_MIN_LENGTH_RULE)
    }
    if (!PASSWORD_UPPERCASE_REGEX.matches(password)) {
        errors.add(PASSWORD_UPPERCASE_RULE)
    }
    if (!PASSWORD_LOWERCASE_REGEX.matches(password)) {
        errors.add(PASSWORD_LOWERCASE_RULE)
    }
    if (!PASSWORD_NUMBER_REGEX.matches(password)) {
        errors.add(PASSWORD_NUMBER_RULE)
    }
    if (!PASSWORD_SPECIAL_CHAR_REGEX.matches(password)) {
        errors.add(PASSWORD_SPECIAL_CHAR_RULE)
    }

    return errors
}

fun formatPasswordErrors(errors: List<String>): String {
    return if (errors.size > 1) {
        "Password is invalid:\n" + errors.joinToString(separator = "\n") { "• $it" }
    } else if (errors.size == 1) {
        "Password is invalid: " + errors.first()
    } else {
        ""
    }
}