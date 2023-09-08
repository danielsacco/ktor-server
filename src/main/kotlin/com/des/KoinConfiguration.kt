package com.des

import com.des.dao.*
import com.des.dao.dbimpl.CustomerDAOImpl
import com.des.dao.dbimpl.OrderDAOImpl
import com.des.dao.dbimpl.ProductDAOImpl
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(appModule(environment.config))
    }
}

fun appModule(appConfig: ApplicationConfig) = module {
    single<DatabaseFactory> { DatabaseFactoryImpl(appConfig) }
    single<CustomerDAO> { CustomerDAOImpl(get()) }
    single<ProductDAO> { ProductDAOImpl(get()) }
    single<OrderDAO> { OrderDAOImpl(get()) }
}