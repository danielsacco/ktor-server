package com.des

import com.des.dao.DatabaseFactory
import com.des.plugins.configureRouting
import com.des.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    DatabaseFactory.init()
}
