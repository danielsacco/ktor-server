package com.des

import com.des.dao.*
import com.des.dao.dbimpl.CustomerDAOImpl
import com.des.dao.dbimpl.OrderDAOImpl
import com.des.dao.dbimpl.ProductDAOImpl
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
    single<DatabaseFactory> { DatabaseFactoryImpl }
    single<CustomerDAO> { CustomerDAOImpl(get()) }
    single<ProductDAO> { ProductDAOImpl() }
    single<OrderDAO> { OrderDAOImpl() }
}