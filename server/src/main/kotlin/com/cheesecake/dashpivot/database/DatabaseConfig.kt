package com.cheesecake.dashpivot.database

import org.jetbrains.exposed.sql.Database

fun connectToDatabase() {
    val dbUrl = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5433/pivotdash"
    val dbUser = System.getenv("DATABASE_USER") ?: "akopyan_albert"
    val dbPassword = System.getenv("DATABASE_PASSWORD") ?: "za1avGU8"

    Database.connect(
        url = dbUrl,
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
}