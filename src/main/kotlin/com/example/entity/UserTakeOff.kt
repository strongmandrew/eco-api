package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserTakeOff(
    val id: Int? = null,
    val idUser: Int,
    val idRecyclePoint: Int,
    val idRubbishType: Int,
    val amountInGrams: Double,
    val datetime: String? = null
)
