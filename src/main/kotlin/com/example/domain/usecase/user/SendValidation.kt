package com.example.domain.usecase.user

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.usecase.email.CodeGenerator
import com.example.domain.usecase.email.EmailService
import com.example.utils.ServiceResult

class SendValidation(
    private val setOrUpdateValidationCode: SetOrUpdateValidationCode,
    private val emailService: EmailService,
    private val codeGenerator: CodeGenerator
) {

    suspend operator fun invoke(email: String): Response<Boolean> {

        val generatedCode = codeGenerator.generateValidationCode()

        return when (val setOrUpdate = setOrUpdateValidationCode
            (email, generatedCode)) {

            is ServiceResult.Success -> {

                when (
                    val mailing = emailService.sendMessage(email,
                    generatedCode
                    )) {

                    is ServiceResult.Success -> {


                        Response(
                            data = mailing.data,
                            statusCode = 200
                        )
                    }

                    is ServiceResult.Error -> {

                        Response(
                            error = ErrorResponse(
                                mailing.error.name, mailing.error.message
                            ),
                            statusCode = mailing.error.statusCode
                        )
                    }
                }

            }

            is ServiceResult.Error -> {

                Response(
                    error = ErrorResponse(
                        setOrUpdate.error.name, setOrUpdate.error.message
                    ),
                    statusCode = setOrUpdate.error.statusCode
                )
            }
        }




    }
}