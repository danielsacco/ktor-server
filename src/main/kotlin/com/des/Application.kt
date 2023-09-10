package com.des

import com.des.daos.DatabaseFactory
import com.des.plugins.configureRouting
import com.des.plugins.configureSerialization
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import org.koin.ktor.ext.inject

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureRouting()
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }
    val databaseFactory by inject<DatabaseFactory>()
    databaseFactory.init()
}
