package com.des.config

import com.des.dao.*
import com.des.dao.dbimpl.CustomerDAOImpl
import com.des.dao.dbimpl.OrderDAOImpl
import com.des.dao.dbimpl.ProductDAOImpl
import com.des.plugins.configureRouting
import com.des.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.testModule() {
    configureTestKoin()
    configureSerialization()
    configureRouting()
    val databaseFactory by inject<DatabaseFactory>()
    databaseFactory.init()
}

fun Application.configureTestKoin() {
    install(Koin) {
        slf4jLogger()
        modules(testKoinModule(environment.config))
    }
}

fun testKoinModule(appConfig: ApplicationConfig) = module {
    single<DatabaseFactory> { DatabaseFactoryServerTest(appConfig) }
    single<ProductDAO> { ProductDAOImpl(get()) }
    single<CustomerDAO> { CustomerDAOImpl(get()) }
    single<OrderDAO> { OrderDAOImpl(get()) }
}
