package com.example.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val TIME_FORMAT = "HH:mm"

fun String.toDatabaseTime(): LocalTime {
    return LocalTime.parse(this, DateTimeFormatter.ofPattern(TIME_FORMAT))
}