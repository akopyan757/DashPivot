package com.cheesecake.dashpivot.database

import org.jetbrains.exposed.sql.Database

fun connectToDatabase() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5433/pivotdash",
        driver = "org.postgresql.Driver",
        user = "akopyan_albert",
        password = "za1avGU8"
    )
}