package com.des.models.db

import com.des.models.OrderDTO
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object Orders : UUIDTable("Orders") {
    val customer = reference("customer_id", Customers)
    val orderDate = datetime("order_date")
    val orderNumber = integer("order_number").autoIncrement()
}

class Order(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Order>(Orders)
    var customer by Customer referencedOn Orders.customer
    var orderDate by Orders.orderDate
    var orderNumber by Orders.orderNumber

    val items by Item referrersOn Items.order
}

fun Order.toDTO(): OrderDTO = OrderDTO(
    uuid = this.id.toString(),
    orderNumber = orderNumber,
    items = items.map { it.toDTO() },
    customerUserName = customer.username,
)

