package com.example.domain.usecase.email

import com.example.utils.Errors
import com.example.utils.ServiceResult
import kotlinx.coroutines.*
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.system.measureTimeMillis

class EmailService(
    session: Session,
) {

    val message = MimeMessage(session)

    suspend fun sendMessage(toEmail: String,
                    code: Int): ServiceResult<Boolean> = coroutineScope {

        return@coroutineScope try {

            setHeaders(message)

            setInfo(message)

            message.setText("Email validation code: $code. Keep it in " +
                    "secret!")

            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toEmail, false))

            measureTimeMillis {
                CoroutineScope(SupervisorJob()).launch {
                    Transport.send(message)
                }
            }.also { println("Elapsed time ${it / 1_000} s.") }

            ServiceResult.Success(true)

        } catch (e: Exception) {

            ServiceResult.Error(Errors.EMAIL_SERVICE_UNAVAILABLE)
        }

    }

    private fun setHeaders(message: MimeMessage) {
        message.addHeader("Content-type", "text/HTML; charset=UTF-8")
        message.addHeader("format", "flowed")
        message.addHeader("Content-Transfer-Encoding", "8bit")
    }

    private fun setInfo(message: MimeMessage) {
        message.setFrom(InternetAddress(EmailCredentials.emailFrom,
            EmailCredentials.emailAccount))

        message.replyTo =InternetAddress.parse("no_reply@ecomap" +
                ".com", false)

        message.sentDate = Date()
    }


}