package com.cheesecake.server.auth.route.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object VerificationCodes : Table("verification_codes") {
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val code = varchar("code", 255)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}