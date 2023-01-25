package com.example.utils

import io.ktor.http.*

enum class Errors(val message: String) {
    DATABASE_ERROR(
        message ="An error occurred trying to reach database",
    ),
    EMPTY_DATA(
        message = "Empty result dataset",
    ),
    INSERT_FAILED(
        message = "Unable to insert data",
    ),
    ID_NOT_FOUND(
        message = "Input id cannot be found in database",
    ),
    UPDATE_FAILED(
        message = "Failed to update database entry",
    ),
    FILE_SYSTEM_ERROR(
        message = "Failed to upload a file"
    ),
    ALREADY_EXISTS(
        message = "Data already exists in database"
    )

}