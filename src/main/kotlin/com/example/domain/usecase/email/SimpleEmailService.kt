package com.example.domain.usecase.email

import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SimpleEmailService(
    session: Session,
    private val toEmail: String,
    private val code: Int
): EmailService(session) {

    override fun setHeaders(message: MimeMessage) {
        message.addHeader("Content-type", "text/HTML; charset=UTF-8")
        message.addHeader("format", "flowed")
        message.addHeader("Content-Transfer-Encoding", "8bit")
    }

    override fun setBody(message: MimeMessage) {
        message.setFrom(InternetAddress("no_reply@ecomap.com",
            "NoReply-Eco"))

        message.replyTo =InternetAddress.parse("no_reply@ecomap" +
                ".com", false)

        message.setText("Email validation code: $code. Keep it in " +
                "secret!")

        message.sentDate = Date()

        message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(toEmail, false))
    }
}