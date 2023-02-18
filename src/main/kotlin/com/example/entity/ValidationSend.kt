package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class ValidationSend(
    val email: String
)
