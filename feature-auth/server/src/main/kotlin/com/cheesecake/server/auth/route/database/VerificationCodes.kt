package com.cheesecake.server.auth.route.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object VerificationCodes : IntIdTable("verification_codes") {
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val code = varchar("code", 255)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}