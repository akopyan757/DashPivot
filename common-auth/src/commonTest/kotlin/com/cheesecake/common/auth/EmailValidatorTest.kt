package com.cheesecake.common.auth

import com.cheesecake.common.auth.utils.isValidEmail
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EmailValidatorTest {

    private val validEmails = listOf(
        "test@example.com",
        "user.name+tag+sorting@example.com",
        "user@example.co.uk",
        "user@subdomain.example.com"
    )

    private val invalidEmails = listOf(
        "plainaddress",
        "@missingusername.com",
        "username@.com",
        "username@domain..com",
        "username@domain.c",
        "username@domain",
        "john.doe2@examplecom",
    )

    @Test
    fun testValidEmail() {
        for (email in validEmails) {
            assertTrue(isValidEmail(email), "Expected $email to be valid")
        }
    }

    @Test
    fun testInvalidEmail() {
        for (email in invalidEmails) {
            assertFalse(isValidEmail(email), "Expected $email to be invalid")
        }
    }
}