package com.example.data.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.time
import java.time.LocalDate
import java.time.LocalDateTime

object UserTable: IntIdTable("user") {
    val firstName = varchar("first_name", 45)
    val lastName = varchar("last_name", 45)
    val email = varchar("email", 128)
    val password = varchar("password", 256)
    val dateOfBirth = date("date_of_birth")
    val dateOfRegistration = date("date_of_registration")
        .clientDefault { LocalDate.now() }
    val image = varchar("image_resource_name", 128)
    val emailVerified = bool("email_verified").default(false)
    val roleId = reference("role_id", RoleTable.id)
}

object EmailBlacklistTable: IntIdTable("email_blacklist") {
    val email = varchar("email", 128)
}

object RoleTable: IntIdTable("role") {
    val role = varchar("role", 45)
    val description = varchar("description", 256).nullable()
}

object NewsTable: IntIdTable("news") {
    val title = varchar("title", 128)
    val imageUrl = varchar("image_url", 128)
    val dateCreated = datetime("date_of")
    val url = varchar("url", 128)
}

object UserEmailCodeTable: IntIdTable("user_email_code") {
    val email = varchar("email", 128)
    val code = integer("code")
}

object RecyclePointTable: IntIdTable("recycle_point") {
    val latitude = double("latitude")
    val longitude = double("longitude")
    val streetName = varchar("street_name", 45)
    val streetHouseNum = varchar("street_house_num", 8)
    val startWorking = time("start_working")
    val endWorking = time("end_working")
    val description = varchar("location_description", 256).nullable()
    val photoPath = varchar("photo_path", 128).nullable()
    val totalRating = integer("total_rating").default(0)
    val totalReviews = integer("total_reviews").default(0)
    val approved = bool("approved").default(false)
    val type = reference("type_id", RecyclePointTypeTable.id)
}

object RecyclePointTypeTable: IntIdTable("recycle_point_type") {
    val type = varchar("type", 45)
    val description = varchar("description", 128).nullable()

}

object ReviewTable: IntIdTable("review") {
    val review = varchar("review", 512)
    val dateCreated = datetime("date_of").clientDefault { LocalDateTime.now() }
    val rating = integer("rating").check("rating_range") {
        (it greater 0) and (it less 6)
    }
    var recyclePoint = reference("recycle_point_id", RecyclePointTable.id)
}

object RubbishTypeTable: IntIdTable("rubbish_type") {
    val type = varchar("type", 45)
    val description = varchar("description", 256).nullable()
}

object RecyclePointRubbishTypeTable: IntIdTable("recycle_point_rubbish_type") {
    val recyclePoint = reference("recycle_point_id", RecyclePointTable.id)
    val rubbishType = reference("rubbish_type_id", RubbishTypeTable.id)
}