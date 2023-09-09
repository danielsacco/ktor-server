package com.des.models.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ProductsTable : IntIdTable("Products") {
    val price = decimal("price", 12, 2)
    val name = varchar("name", 64)
    val description = varchar("description", 128)
}

class ProductEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductEntity>(ProductsTable)
    var price by ProductsTable.price
    var name by ProductsTable.name
    var description by ProductsTable.description
}
