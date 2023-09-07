package com.des.models.db

import com.des.models.ProductDTO
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Products : UUIDTable("Products") {
    val price = decimal("price", 12, 2)
    val name = varchar("name", 64)
    val description = varchar("description", 128)
}

class Product(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Product>(Products)
    var price by Products.price
    var name by Products.name
    var description by Products.description
}

fun Product.toDTO() = ProductDTO(
    price = price.toDouble(),
    name = name,
    description = description,
    uuid = id.toString()
)