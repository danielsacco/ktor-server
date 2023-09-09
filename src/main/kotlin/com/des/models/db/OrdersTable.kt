package com.des.models.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object OrdersTable : IntIdTable("Orders") {
    val customer = reference("customer_id", CustomersTable)
    val orderDate = datetime("order_date")
    val orderNumber = integer("order_number").autoIncrement()
}

class OrderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderEntity>(OrdersTable)
    var customerEntity by CustomerEntity referencedOn OrdersTable.customer
    var orderDate by OrdersTable.orderDate
    var orderNumber by OrdersTable.orderNumber

    val items by ItemEntity referrersOn ItemsTable.order
}


