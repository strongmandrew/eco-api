package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserTakeOff(
    val id: Int? = null,
    val idUser: Int? = null,
    val idRecyclePoint: Int? = null,
    val idRubbishType: Int,
    val amountInGrams: Double,
    val datetime: String? = null,
    val percentRating: Int
)
