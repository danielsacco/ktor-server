package com.des.models.db

import com.des.models.OrderDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Orders : IntIdTable("Orders") {
    val customer = reference("customer_id", Customers)
    val orderDate = datetime("order_date")
    val orderNumber = integer("order_number").autoIncrement()
}

class Order(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Order>(Orders)
    var customer by Customer referencedOn Orders.customer
    var orderDate by Orders.orderDate
    var orderNumber by Orders.orderNumber

    val items by Item referrersOn Items.order
}

fun Order.toDTO(): OrderDTO = OrderDTO(
    id = id.value,
    orderNumber = orderNumber,
    items = items.map { it.toDTO() },
    customerUserName = customer.username,
)

