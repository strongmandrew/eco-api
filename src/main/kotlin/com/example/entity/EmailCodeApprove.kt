package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class EmailCodeApprove(
    val email: String,
    val code: Int
)
