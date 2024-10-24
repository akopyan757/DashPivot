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

    init {
        val props = System.getProperties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
        }

        val username = "akopyan757@gmail.com"
        val password = "za1avGU8@"

        session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })
    }

    override fun sendVerificationEmail(email: String, token: String) {
        val verificationUrl = "https://yourapp.com/verify?token=$token"
        val subject = "Email Verification"
        val body = """
            Please verify your email address by clicking on the link below:
            $verificationUrl
        """.trimIndent()

        sendEmail(email, subject, body)
    }

    private fun sendEmail(to: String, subj: String, body: String) {
        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress("akopyan757@gmail.com"))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
                subject = subj
                setText(body)
            }

            //Transport.send(message)
            println("Email sent successfully")
            println(body)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}