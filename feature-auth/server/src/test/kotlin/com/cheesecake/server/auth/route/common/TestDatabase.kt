package com.cheesecake.server.auth.route.common

import com.cheesecake.server.auth.route.database.Users
import com.cheesecake.server.auth.route.database.VerificationCodes
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object TestDatabase {

    private val database by lazy {
        Database.connect("jdbc:sqlite:test_database.db", driver = "org.sqlite.JDBC")
    }

    fun setupTestDatabase() {
        database
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Users, VerificationCodes)
        }
    }

    fun dropTestDatabase() {
        transaction {
            SchemaUtils.drop(Users, VerificationCodes)
        }
    }
}