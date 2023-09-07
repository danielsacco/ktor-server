package com.des

import com.des.dao.DatabaseFactory
import com.des.plugins.configureRouting
import com.des.plugins.configureSerialization
import com.des.plugins.configureTemplating
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureTemplating()
    configureSerialization()
    configureRouting()
    DatabaseFactory.init()
}
