package com.des

import com.des.daos.DatabaseFactory
import com.des.plugins.configureRouting
import com.des.plugins.configureSerialization
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.testModule() {
    configureTestKoin()
    configureSerialization()
    configureRouting()
    val databaseFactory by inject<DatabaseFactory>()
    databaseFactory.init()
}


