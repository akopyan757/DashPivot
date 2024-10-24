package com.cheesecake.server.auth.route.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object Users : Table() {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val isVerified = bool("is_verified").default(false)
    val verificationToken = varchar("verification_token", 255).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val lastLogin = datetime("last_login").nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}