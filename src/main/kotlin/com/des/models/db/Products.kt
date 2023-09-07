package com.des.models.db

import com.des.models.ProductDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Products : IntIdTable("Products") {
    val price = decimal("price", 12, 2)
    val name = varchar("name", 64)
    val description = varchar("description", 128)
}

class Product(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Product>(Products)
    var price by Products.price
    var name by Products.name
    var description by Products.description
}

fun Product.toDTO() = ProductDTO(
    price = price.toDouble(),
    name = name,
    description = description,
    id = id.value
)