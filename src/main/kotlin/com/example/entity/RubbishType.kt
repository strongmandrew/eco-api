package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class RubbishType(
    val id: Int? = null,
    val type: String,
    val description: String? = null
)
