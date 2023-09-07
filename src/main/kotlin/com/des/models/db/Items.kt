package com.des.models.db

import com.des.models.ItemDTO
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Items : UUIDTable("Items") {
    val order = reference("order_id", Orders)
    val product = reference("product_id", Products)
    val quantity = integer("quantity")
}

class Item(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Item>(Items)
    var order by Order referencedOn Items.order
    var product by Product referencedOn Items.product
    var quantity by Items.quantity
}

fun Item.toDTO() : ItemDTO = ItemDTO(
    productId = this.product.id.toString(),
    amount = this.quantity,
)