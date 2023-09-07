package com.des.models.db

import com.des.models.ItemDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Items : IntIdTable("Items") {
    val order = reference("order_id", Orders)
    val product = reference("product_id", Products)
    val quantity = integer("quantity")
}

class Item(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Item>(Items)
    var order by Order referencedOn Items.order
    var product by Product referencedOn Items.product
    var quantity by Items.quantity
}

fun Item.toDTO() : ItemDTO = ItemDTO(
    productId = this.product.id.toString(),
    amount = this.quantity,
)