package com.cheesecake.server.auth.route.database

import com.cheesecake.common.auth.model.User
import com.cheesecake.server.auth.route.database.Users.verificationToken
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object UserSource {

    suspend fun isEmailTaken(email: String): Boolean {
        return transaction {
            Users.selectAll().where { Users.email eq email }.count() > 0
        }
    }

    fun findUserByToken(token: String): User? {
        return transaction {
            Users.selectAll().where { verificationToken eq token }.singleOrNull()?.mapToUser()
        }
    }

    fun verifyEmail(id: Int) {
        transaction {
            Users.update({ Users.id eq id }) {
                it[isVerified] = true
            }
        }
    }

    fun createUser(email: String, passwordHash: String, isVerified: Boolean, verificationToken: String): User {
        return transaction {
            Users.insert {
                it[Users.email] = email
                it[Users.passwordHash] = passwordHash
                it[Users.isVerified] = isVerified
                it[Users.verificationToken] = verificationToken
                it[Users.createdAt] = CurrentDateTime
            }
            Users.selectAll().where { Users.email eq email }
                .first()
                .mapToUser()
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