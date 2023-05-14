package com.example.data

import com.example.data.database.DatabaseFactory.dbQuery
import com.example.data.database.EmailBlacklistTable
import com.example.data.database.UserEmailCodeTable
import com.example.data.database.UserTable
import com.example.domain.dao.UserDao
import com.example.domain.usecase.user.PasswordEncrypt
import com.example.entity.User
import com.example.utils.Errors
import com.example.utils.ServiceResult
import com.example.utils.toDatabaseDate
import org.jetbrains.exposed.sql.*

class UserDaoImpl(
    private val passwordEncrypt: PasswordEncrypt,
) : UserDao {

    override suspend fun registerUser(user: User) = try {
        dbQuery {
            UserTable.insert {
                it[firstName] = user.firstName
                it[lastName] = user.lastName
                it[email] = user.email
                it[password] = passwordEncrypt(
                    user.password ?: throw IllegalStateException()
                )
                it[dateOfBirth] = user.dateOfBirth.toDatabaseDate()
                it[image] = user.userImage
            }.resultedValues?.singleOrNull()?.let {
                ServiceResult.Success(it.toUser())
            } ?: ServiceResult.Error(Errors.INSERT_FAILED)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun getUserById(userId: Int) = try {
        dbQuery {
            UserTable.select { UserTable.id eq userId }
                .singleOrNull()?.let {
                    ServiceResult.Success(it.toUser())

                } ?: ServiceResult.Error(Errors.NOT_FOUND)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun userEmailVerified(idUser: Int) = try {
        dbQuery {
            if (UserTable.select {
                    (UserTable.id eq idUser) and
                            (UserTable.emailVerified eq true)
                }.count() > 0)

                ServiceResult.Success(true)
            else ServiceResult.Error(Errors.EMAIL_NOT_VERIFIED)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun incrementTimesChanged(
        idUser: Int,
        previousTimesChanged: Int,
    ) = try {
        dbQuery {
            if (UserTable.update(
                    where = { UserTable.id eq idUser },
                    body = {
                        it[timesChanged] =
                            previousTimesChanged + 1
                    }
                ) > 0
            ) ServiceResult.Success(true)
            else ServiceResult.Error(Errors.UPDATE_FAILED)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun checkUserCredentials(
        email: String,
        password: String,
    ) = try {
        dbQuery {
            UserTable.select {
                (UserTable.email eq email) and
                        (UserTable.password eq password)
            }.singleOrNull()?.let {
                ServiceResult.Success(it.toUser())
            } ?: ServiceResult.Error(Errors.NOT_FOUND)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun getUserByEmail(email: String) = try {
        dbQuery {
            UserTable.select {
                UserTable.email eq
                        email
            }.singleOrNull()?.let {
                ServiceResult.Success(it.toUser())
            } ?: ServiceResult.Error(Errors.NOT_FOUND)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun updateValidationCode(
        email: String,
        code: Int,
    ) = try {
        dbQuery {
            if (UserEmailCodeTable.update(
                    where = { UserEmailCodeTable.email eq email },
                    body = { it[UserEmailCodeTable.code] = code }
                ) > 0
            )
                ServiceResult.Success(true)
            else
                ServiceResult.Error(Errors.UPDATE_FAILED)
        }
    } catch (e: Exception) {

        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun setValidationCode(
        email: String,
        code: Int,
    ) = try {
        dbQuery {
            UserEmailCodeTable.insert {
                it[UserEmailCodeTable.email] = email
                it[UserEmailCodeTable.code] = code
            }.resultedValues?.toList()?.let {
                ServiceResult.Success(true)

            } ?: ServiceResult.Error(Errors.INSERT_FAILED)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun compareValidationCode(
        email: String,
        code: Int,
    ) = try {

        dbQuery {
            val codesMatch = UserEmailCodeTable.select {
                (UserEmailCodeTable
                    .email eq email) and (UserEmailCodeTable.code
                        eq code)
            }.count() > 0

            ServiceResult.Success(codesMatch)

        }

    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun approveUserEmail(
        email: String,
    ) = try {
        dbQuery {
            if (UserTable.update(
                    where = { UserTable.email eq email },
                    body = {
                        it[emailVerified] = true
                    }) > 0
            )
                ServiceResult.Success(true)
            else ServiceResult.Error(Errors.UPDATE_FAILED)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun changeUserPasswordById(
        idUser: Int,
        password: String,
    ) =
        try {
            dbQuery {
                if (UserTable.update(
                        where = { UserTable.id eq idUser },
                        body = { it[UserTable.password] = password }
                    ) > 0
                ) ServiceResult.Success(true)
                else ServiceResult.Error(Errors.UPDATE_FAILED)
            }
        } catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }

    override suspend fun emailDoesNotExist(
        email: String,
    ) = try {
        dbQuery {
            val exists = UserTable.select {
                UserTable.email eq
                        email
            }.count() < 1
            ServiceResult.Success(exists)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun noEntriesOfEmail(
        email: String,
    ) = try {
        dbQuery {
            val exists = UserEmailCodeTable.select {
                UserEmailCodeTable
                    .email eq email
            }.count() == 0L

            ServiceResult.Success(exists)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun isEmailNotInBlacklist(
        email: String,
    ) = try {
        dbQuery {
            val blacklisted = EmailBlacklistTable.select {
                EmailBlacklistTable
                    .email eq email
            }.count() > 0

            ServiceResult.Success(blacklisted)
        }

    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    private fun ResultRow.toUser() = User(
        id = this[UserTable.id].value,
        firstName = this[UserTable.firstName],
        lastName = this[UserTable.lastName],
        email = this[UserTable.email],
        dateOfBirth = this[UserTable.dateOfBirth].toString(),
        dateOfRegistration = this[UserTable.dateOfRegistration]
            .toString(),
        userImage = this[UserTable.image],
        emailVerified = this[UserTable.emailVerified],
        timesChanged = this[UserTable.timesChanged],
        role = this[UserTable.roleId]
    )
}