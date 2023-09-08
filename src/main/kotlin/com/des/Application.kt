package com.des

import com.des.dao.DatabaseFactory
import com.des.plugins.configureRouting
import com.des.plugins.configureSerialization
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureRouting()
    val databaseFactory by inject<DatabaseFactory>()
    databaseFactory.init()
}
