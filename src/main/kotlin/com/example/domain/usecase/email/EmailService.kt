package com.example.domain.usecase.email

import com.example.utils.Errors
import com.example.utils.ServiceResult
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.MimeMessage

abstract class EmailService(
    private val session: Session,

) {

    val message = MimeMessage(session)

    fun sendMessage(): ServiceResult<Boolean> {

        return try {

            setHeaders(message)

            setBody(message)

            Transport.send(message)

            ServiceResult.Success(true)

        }
        catch (e: Exception) {

            ServiceResult.Error(Errors.EMAIL_SERVICE_UNAVAILABLE)
        }

    }

    abstract fun setHeaders(message: MimeMessage)

    abstract fun setBody(message: MimeMessage)


}