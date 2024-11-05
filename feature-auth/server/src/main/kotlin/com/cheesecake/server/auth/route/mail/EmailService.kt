package com.cheesecake.server.auth.route.mail

import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailService: IEmailService {
    private val session: Session

    val senderEmail: String

    init {
        val props = System.getProperties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
        }

        senderEmail = System.getenv("EMAIL_USERNAME") ?: "your_email@gmail.com"
        val password = System.getenv("EMAIL_PASSWORD") ?: "your_app_password"

        session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(senderEmail, password)
            }
        })
    }

    override fun sendVerificationEmail(email: String, code: String) {
        val subject = "Email Verification"
        val body = """
             Please verify your email address by entering the following verification code:
             $code
        """.trimIndent()

        sendEmail(senderEmail, email, subject, body)
    }

    private fun sendEmail(from: String, to: String, subject: String, body: String) {
        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(from))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
                this.subject = subject
                setText(body)
            }

            Transport.send(message)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}