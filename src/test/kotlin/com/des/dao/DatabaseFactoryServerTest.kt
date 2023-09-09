package com.des.dao

import com.des.models.db.CustomersTable
import com.des.models.db.ItemsTable
import com.des.models.db.OrdersTable
import com.des.models.db.ProductsTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseFactoryServerTest(private val config: ApplicationConfig) : DatabaseFactory {

    lateinit var source: HikariDataSource

    private fun createHikariDataSource(
        url: String,
        driver: String
    ) = HikariDataSource(HikariConfig().apply {
        driverClassName = driver
        jdbcUrl = url
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    })

    override fun init() {
        val driverClassName = config.property("storage.driverClassName").getString()
        val jdbcURL = config.property("storage.jdbcURL").getString()
        source = createHikariDataSource(jdbcURL, driverClassName)
        val database = Database.connect(source)
        transaction(database) {
            SchemaUtils.create(CustomersTable)
            SchemaUtils.create(ProductsTable)
            SchemaUtils.create(ItemsTable)
            SchemaUtils.create(OrdersTable)
        }
    }

    override suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}