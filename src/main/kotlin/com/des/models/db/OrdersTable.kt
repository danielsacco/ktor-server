package com.des.models.db

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object OrdersTable : UUIDTable("Orders") {
    val customer = reference("customer_id", CustomersTable)
    val orderDate = datetime("order_date")
    val orderNumber = integer("order_number").autoIncrement()
}

class OrderEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<OrderEntity>(OrdersTable)
    var customerEntity by CustomerEntity referencedOn OrdersTable.customer
    var orderDate by OrdersTable.orderDate
    var orderNumber by OrdersTable.orderNumber

    val items by ItemEntity referrersOn ItemsTable.order
}


