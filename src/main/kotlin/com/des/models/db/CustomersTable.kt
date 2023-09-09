package com.des.models.db

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object CustomersTable : UUIDTable("Customers") {
    val firstName = varchar("firstName", 64)
    val lastName = varchar("lastName", 64)
    val email = varchar("email", 64)
    val username = varchar("username", 64)
}

class CustomerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CustomerEntity>(CustomersTable)

    var firstName by CustomersTable.firstName
    var lastName by CustomersTable.lastName
    var email by CustomersTable.email
    var username by CustomersTable.username

    val orders by OrderEntity referrersOn OrdersTable.customer
}
