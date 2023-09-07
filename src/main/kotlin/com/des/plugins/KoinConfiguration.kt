package com.des.plugins

import com.des.dao.*
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}

val appModule = module {
    single<CustomerDAO> { CustomerDAOImpl() }
    single<ProductDAO> { ProductDAOImpl() }
    single<OrderDAO> { OrderDAOImpl() }
}