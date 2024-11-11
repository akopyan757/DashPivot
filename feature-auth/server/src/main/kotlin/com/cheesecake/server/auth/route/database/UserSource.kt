package com.cheesecake.server.auth.route.database

import com.cheesecake.common.auth.config.Config.VERIFICATION_CODE_SENDING_DELAY_SEC
import com.cheesecake.common.auth.model.User
import com.cheesecake.common.auth.model.UserVerify
import com.cheesecake.server.auth.route.common.dateFormatter
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertReturning
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object UserSource {

    fun isEmailTakenAndVerified(email: String): Boolean {
        return transaction {
            Users.selectAll().where { (Users.email eq email) and Users.isVerified }.count() > 0
        }
    }

    fun isVerificationCodeAvailable(email: String): Boolean {
        val lastVerificationRecord = findUserForVerification(email)
        lastVerificationRecord?.let {
            val now = LocalDateTime.now()
            val lastSentTime = it.createdAt?.let { dateTime ->
                LocalDateTime.parse(dateTime, dateFormatter)
            }
            return lastSentTime?.isBefore(
                now.minus(VERIFICATION_CODE_SENDING_DELAY_SEC, ChronoUnit.SECONDS)
            ) ?: true
        }
        return true
    }

    fun verifyEmail(id: Int) {
        transaction {
            Users.update({ Users.id eq id }) {
                it[isVerified] = true
            }
            VerificationCodes.deleteWhere { userId eq id }
        }
    }

    fun createUser(
        email: String,
        passwordHash: String,
        isVerified: Boolean,
        verificationToken: String
    ): User {
        return transaction {
            val existingUser = Users.selectAll().where { Users.email eq email }.singleOrNull()

            if (existingUser != null) {
                if (!existingUser[Users.isVerified]) {
                    Users.update({ Users.email eq email }) {
                        it[Users.passwordHash] = passwordHash
                        it[Users.isVerified] = isVerified
                        it[createdAt] = CurrentDateTime
                    }

                    if (isVerified) {
                        VerificationCodes.deleteWhere { userId eq existingUser[Users.id] }
                    } else {
                        VerificationCodes.insert {
                            it[userId] = existingUser[Users.id]
                            it[code] = verificationToken
                            it[createdAt] = CurrentDateTime
                        }
                    }
                }
            } else {
                val id = Users.insertReturning(listOf(Users.id)) {
                    it[Users.email] = email
                    it[Users.passwordHash] = passwordHash
                    it[Users.isVerified] = isVerified
                    it[createdAt] = CurrentDateTime
                }.map { it[Users.id] }.singleOrNull()

                if (!isVerified && id != null) {
                    VerificationCodes.insert {
                        it[userId] = id
                        it[code] = verificationToken
                        it[createdAt] = CurrentDateTime
                    }
                }
            }

            Users.selectAll().where { Users.email eq email }
                .single()
                .mapToUser()
        }
    }

    fun findUserByEmail(email: String): User? {
        return transaction {
            Users.selectAll().where { Users.email eq email }
                .singleOrNull()
                ?.mapToUser()
        }
    }

    fun findUserForVerification(email: String): UserVerify? {
        return transaction {
            (Users leftJoin VerificationCodes)
                .selectAll().where { Users.email eq email }
                .singleOrNull()
                ?.let {
                    UserVerify(
                        id = it[Users.id],
                        email = it[Users.email],
                        verificationHashedCode = it[VerificationCodes.code],
                        createdAt = it[VerificationCodes.createdAt].format(dateFormatter)
                    )
                }
        }
    }

    private fun ResultRow.mapToUser(): User {
        return User(
            id = this[Users.id],
            email = this[Users.email],
            passwordHash = this[Users.passwordHash],
            isVerified = this[Users.isVerified]
        )
    }
}