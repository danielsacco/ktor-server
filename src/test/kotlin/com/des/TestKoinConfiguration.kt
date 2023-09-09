package com.des

import com.des.daos.*
import com.des.daos.dbimpl.CustomerDAOImpl
import com.des.daos.dbimpl.OrderDAOImpl
import com.des.daos.dbimpl.ProductDAOImpl
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

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