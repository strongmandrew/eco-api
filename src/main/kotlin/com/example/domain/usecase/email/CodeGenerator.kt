package com.example.domain.usecase.email

import kotlin.random.Random

class CodeGenerator {

    fun generateValidationCode(): Int {
        return Random.nextInt(100_000, 999_999)
    }
}