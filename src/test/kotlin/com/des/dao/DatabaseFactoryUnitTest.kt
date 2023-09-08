package com.des.dao

import com.des.models.db.Customers
import com.des.models.db.Items
import com.des.models.db.Orders
import com.des.models.db.Products
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseFactoryUnitTest : DatabaseFactory {

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
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:mem:"
        source = createHikariDataSource(jdbcURL, driverClassName)
        val database = Database.connect(source)
        transaction(database) {
            SchemaUtils.create(Customers)
            SchemaUtils.create(Products)
            SchemaUtils.create(Items)
            SchemaUtils.create(Orders)
        }
    }

    fun close() {
        source.close()
    }
    override suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}