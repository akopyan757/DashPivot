package com.cheesecake.server.auth.route.mail

interface IEmailService {
    fun sendVerificationEmail(email: String, code: String)
}