package com.des.models.db

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object ProductsTable : UUIDTable("Products") {
    val price = decimal("price", 12, 2)
    val name = varchar("name", 64)
    val description = varchar("description", 128)
}

class ProductEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ProductEntity>(ProductsTable)
    var price by ProductsTable.price
    var name by ProductsTable.name
    var description by ProductsTable.description
}
