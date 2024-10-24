package com.cheesecake.common.auth

import com.cheesecake.common.auth.utils.isValidPassword
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class PasswordValidatorTest {

    private val validPasswords = listOf(
        "StrongPass1!",
        "AnotherValid1@",
        "Password123!",
        "MyP@ssword2024"
    )

    private val invalidPasswords = listOf(
        "short",                     // Слишком короткий
        "nouppercase123@",           // Нет заглавной буквы
        "NOLOWERCASE123!",           // Нет строчной буквы
        "NoSpecialChar123",          // Нет специального символа
        "12345678",                  // Нет букв
        "password",                  // Нет цифр и специальных символов
        "password!"                  // Нет цифр
    )

    @Test
    fun testValidPassword() {
        for (password in validPasswords) {
            assertTrue(isValidPassword(password), "Expected '$password' to be valid")
        }
    }

    @Test
    fun testInvalidPassword() {
        for (password in invalidPasswords) {
            assertFalse(isValidPassword(password), "Expected '$password' to be invalid")
        }
    }
}
