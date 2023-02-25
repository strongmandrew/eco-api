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
import java.lang.IllegalStateException

class UserDaoImpl(
    private val passwordEncrypt: PasswordEncrypt
) : UserDao {

    override suspend fun registerUser(user: User): ServiceResult<User> {
        return try {
            dbQuery {
                UserTable.insert {
                    it[firstName] = user.firstName
                    it[lastName] = user.lastName
                    it[email] = user.email
                    it[password] = passwordEncrypt(user.password ?:
                    throw IllegalStateException())
                    it[dateOfBirth] = user.dateOfBirth.toDatabaseDate()
                    it[image] = user.userImage
                }.resultedValues?.singleOrNull()?.let {
                    ServiceResult.Success(rowToUser(it))
                } ?: ServiceResult.Error(Errors.INSERT_FAILED)
            }
        }
        catch (e: Exception) {
            println(e)
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun getUserById(userId: Int): ServiceResult<User> {
        return try {

            dbQuery {

                UserTable.select { UserTable.id eq userId }
                    .singleOrNull()?.let {

                        ServiceResult.Success(rowToUser(it))

                    } ?: ServiceResult.Error(Errors.ID_NOT_FOUND)
            }

        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun userEmailVerified(idUser: Int): ServiceResult<Boolean> {
        return try {

            dbQuery {
                if (UserTable.select { (UserTable.id eq idUser) and
                            (UserTable.emailVerified eq true) }.count() > 0)

                        ServiceResult.Success(true)

                else ServiceResult.Error(Errors.EMAIL_NOT_VERIFIED)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun checkUserCredentials(
        email: String,
        password: String,
    ): ServiceResult<User> {

        return try {
            dbQuery {
                UserTable.select { (UserTable.email eq email) and
                        (UserTable.password eq password)  }
                    .singleOrNull()?.let {

                        ServiceResult.Success(rowToUser(it))

                    } ?: ServiceResult.Error(Errors.ID_NOT_FOUND)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun getUserByEmail(email: String):
            ServiceResult<User> {
        return try {
            dbQuery {
                UserTable.select { UserTable.email eq
                        email }.singleOrNull()?.let {
                           ServiceResult.Success(rowToUser(it))
                } ?: ServiceResult.Error(Errors.ID_NOT_FOUND)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun updateValidationCode(
        email: String,
        code: Int,
    ): ServiceResult<Boolean> {

        return try {
            dbQuery {
                if (UserEmailCodeTable.update(
                    where = { UserEmailCodeTable.email eq email },
                    body = { it[UserEmailCodeTable.code] = code }
                ) > 0)
                    ServiceResult.Success(true)

                else
                    ServiceResult.Error(Errors.UPDATE_FAILED)
            }
        }
        catch (e: Exception) {

            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun setValidationCode(email: String, code: Int):
            ServiceResult<Boolean> {

        return try {
            dbQuery {
                UserEmailCodeTable.insert {
                    it[UserEmailCodeTable.email] = email
                    it[UserEmailCodeTable.code] = code
                }.resultedValues?.toList()?.let {
                    ServiceResult.Success(true)

                } ?: ServiceResult.Error(Errors.INSERT_FAILED)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun compareValidationCode(
        email: String,
        code: Int,
    ): ServiceResult<Boolean> {

        return try {

            dbQuery {

                if (UserEmailCodeTable.select { (UserEmailCodeTable
                    .email eq email) and (UserEmailCodeTable.code
                            eq code) }.count() > 0)

                    ServiceResult.Success(true)

                else ServiceResult.Error(Errors.ID_NOT_FOUND)

            }

        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun approveUserEmail(email: String): ServiceResult<Boolean> {
        return try {
            dbQuery {

                if (UserTable.update(
                    where = { UserTable.email eq email },
                    body = {
                                             it[emailVerified] = true
                }) > 0)
                    ServiceResult.Success(true)

                else ServiceResult.Error(Errors.UPDATE_FAILED)
            }
        }

        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun emailDoesNotExist(email: String): ServiceResult<Boolean> {
        return try {
            dbQuery {
                if (UserTable.select { UserTable.email eq email }
                    .count() < 1)
                    ServiceResult.Success(true)
                else
                    ServiceResult.Error(Errors.ALREADY_EXISTS)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun noEntriesOfEmail(email: String):
            ServiceResult<Boolean> {
        return try {
            dbQuery {
                if (UserEmailCodeTable.select { UserEmailCodeTable
                    .email eq email }.count() > 0)

                    ServiceResult.Success(false)

                else ServiceResult.Success(true)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun isEmailNotInBlacklist(email: String):
            ServiceResult<Boolean> {
        return try {
            dbQuery {
                if (EmailBlacklistTable.select { EmailBlacklistTable
                    .email eq email }.count() > 0)

                    ServiceResult.Success(false)

                else ServiceResult.Success(true)
            }

        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    private fun rowToUser(row: ResultRow) : User {

        return User(
            id = row[UserTable.id].value,
            firstName = row[UserTable.firstName],
            lastName = row[UserTable.lastName],
            email = row[UserTable.email],
            dateOfBirth = row[UserTable.dateOfBirth].toString(),
            dateOfRegistration = row[UserTable.dateOfRegistration]
                .toString(),
            userImage = row[UserTable.image],
            emailVerified = row[UserTable.emailVerified],
            timesChanged = row[UserTable.timesChanged],
            role = row[UserTable.roleId]
        )
    }
}