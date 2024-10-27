package com.cheesecake.dashpivot.database

import org.jetbrains.exposed.sql.Database

@Suppress("AuthLeak")
fun connectToDatabase() {
    val databaseUrl = System.getenv("DATABASE_URL")

    if (databaseUrl != null) {
        val regex = Regex("postgres://(.*):(.*)@(.*):(\\d+)/(.*)")
        val matchResult = regex.find(databaseUrl)
        if (matchResult != null) {
            val (username, password, hostname, port, databaseName) = matchResult.destructured

            val jdbcUrl = "jdbc:postgresql://$hostname:$port/$databaseName"

            Database.connect(
                url = jdbcUrl,
                driver = "org.postgresql.Driver",
                user = username,
                password = password
            )
        } else {
            throw IllegalStateException("DATABASE_URL environment variable is not in the expected format.")
        }
    } else {
        val localUrl = "jdbc:postgresql://localhost:5433/pivotdash"
        val localUser = "akopyan_albert"
        val localPassword = "za1avGU8"

        Database.connect(
            url = localUrl,
            driver = "org.postgresql.Driver",
            user = localUser,
            password = localPassword
        )
    }
}