package com.des

import com.des.dao.DatabaseFactory
import com.des.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    configureTemplating()
    configureRouting()
}
