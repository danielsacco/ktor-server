package com.des

import com.des.dao.*
import com.des.dao.dbimpl.CustomerDAOImpl
import com.des.dao.dbimpl.OrderDAOImpl
import com.des.dao.dbimpl.ProductDAOImpl
import com.des.dao.dbimpl.ProductDAOImplTest
import com.des.models.CustomerDTO
import com.des.models.OrderDTO
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

class CustomerDaoTest: CustomerDAO {

    private var id: Int = 0
    private fun nextId() = id++

    private val repository = mutableListOf<CustomerDTO>()

    override suspend fun customers(): List<CustomerDTO> = repository.toList()

    override suspend fun findCustomerByUsername(username: String): CustomerDTO? =
        repository.firstOrNull { username == it.username }

    override suspend fun createCustomer(customer: CustomerDTO): CustomerDTO? {
        return customer.copy(id = nextId()).also(repository::add)
    }

    override suspend fun deleteCustomer(username: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun customerOrders(customerId: String): List<OrderDTO> {
        TODO("Not yet implemented")
    }

}