package com.des

import com.des.dao.DatabaseFactoryImpl
import com.des.plugins.configureRouting
import com.des.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.testModule() {
    configureKoin()
    configureSerialization()
    configureRouting()
    DatabaseFactoryImpl.init(environment.config)
}
