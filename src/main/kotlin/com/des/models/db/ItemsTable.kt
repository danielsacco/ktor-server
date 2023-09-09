package com.des.models.db

import com.des.models.Item
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ItemsTable : IntIdTable("Items") {
    val order = reference("order_id", OrdersTable)
    val product = reference("product_id", ProductsTable)
    val quantity = integer("quantity")
}

class ItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ItemEntity>(ItemsTable)
    var orderEntity by OrderEntity referencedOn ItemsTable.order
    var productEntity by ProductEntity referencedOn ItemsTable.product
    var quantity by ItemsTable.quantity
}

fun ItemEntity.toDTO() : Item = Item(
    product = this.productEntity.toDTO(),
    amount = this.quantity,
    id = this.id.value
)