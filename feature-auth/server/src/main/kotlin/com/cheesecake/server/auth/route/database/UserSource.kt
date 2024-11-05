package com.cheesecake.server.auth.route.database

import com.cheesecake.common.auth.model.User
import com.cheesecake.common.auth.model.UserVerify
import com.cheesecake.server.auth.route.database.Users.verificationToken
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object UserSource {

    fun isEmailTakenAndVerified(email: String): Boolean {
        return transaction {
            Users.selectAll().where { (Users.email eq email) and Users.isVerified }.count() > 0
        }
    }

    fun verifyEmail(id: Int) {
        transaction {
            Users.update({ Users.id eq id }) {
                it[isVerified] = true
            }
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
                        it[Users.verificationToken] = verificationToken
                        it[createdAt] = CurrentDateTime
                    }
                }
            } else {
                Users.insert {
                    it[Users.email] = email
                    it[Users.passwordHash] = passwordHash
                    it[Users.isVerified] = isVerified
                    it[Users.verificationToken] = verificationToken
                    it[createdAt] = CurrentDateTime
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
            Users.selectAll().where { Users.email eq email }
                .singleOrNull()
                ?.let {
                    UserVerify(
                        id = it[Users.id],
                        email = it[Users.email],
                        verificationHashedCode = it[verificationToken]
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