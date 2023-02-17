package com.example.utils

enum class Errors(val message: String, val statusCode: Int = 500) {
    DATABASE_ERROR(
        message ="An error occurred trying to reach database",
    ),
    EMPTY_DATA(
        message = "Empty result dataset",
        statusCode = 202
    ),
    INSERT_FAILED(
        message = "Unable to insert data",
    ),
    ID_NOT_FOUND(
        message = "Input id cannot be found in database",
        statusCode = 202
    ),
    UPDATE_FAILED(
        message = "Failed to update database entry",
    ),
    FILE_SYSTEM_ERROR(
        message = "Failed to upload a file"
    ),
    ALREADY_EXISTS(
        message = "Data already exists in database",
        statusCode = 202
    ),
    EMAIL_SERVICE_UNAVAILABLE(
        message = "Email service is temporally unavailable",
        statusCode = 500
    ),

}