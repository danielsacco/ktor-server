package com.des.models.db

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object ItemsTable : UUIDTable("Items") {
    val order = reference("order_id", OrdersTable)
    val product = reference("product_id", ProductsTable)
    val quantity = integer("quantity")
}

class ItemEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ItemEntity>(ItemsTable)
    var orderEntity by OrderEntity referencedOn ItemsTable.order
    var productEntity by ProductEntity referencedOn ItemsTable.product
    var quantity by ItemsTable.quantity
}
