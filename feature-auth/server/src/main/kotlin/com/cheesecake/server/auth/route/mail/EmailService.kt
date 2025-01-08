package com.cheesecake.server.auth.route.mail

import com.cheesecake.common.auth.model.sendCode.SendCodeType
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

internal class EmailService: IEmailService {
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

    override fun sendVerificationEmail(
        email: String,
        code: String,
        sendCodeType: SendCodeType
    ): Boolean {
        val subject = when (sendCodeType) {
            SendCodeType.REGISTRATION -> "Registration Verification"
            SendCodeType.RESET_PASSWORD -> "Reset Password Verification"
        }
        val body = """
             Please verify your account by entering the following verification code:
             $code
        """.trimIndent()

        return sendEmail(senderEmail, email, subject, body)
    }

    private fun sendEmail(from: String, to: String, subject: String, body: String): Boolean {
        return try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(from))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
                this.subject = subject
                setText(body)
            }

            Transport.send(message)
            println("EmailService: Message sent successfully\nText = $body")
            true
        } catch (e: MessagingException) {
            println("EmailService: Error sending email\n${e.message}")
            e.printStackTrace()
            false
        }
    }
}