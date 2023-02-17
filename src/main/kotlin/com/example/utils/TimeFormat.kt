package com.example.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val TIME_FORMAT = "HH:mm"
const val DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
const val DATE_FORMAT = "yyyy-MM-dd"

fun String.toDatabaseTime(): LocalTime {
    return LocalTime.parse(this, DateTimeFormatter.ofPattern(TIME_FORMAT))
}

fun String.toDatabaseDatetime(): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern(
        DATETIME_FORMAT))
}

fun String.toDatabaseDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(
        DATE_FORMAT))

}