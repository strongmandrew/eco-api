package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class ValidationApprove(
    val email: String,
    val code: Int
)
