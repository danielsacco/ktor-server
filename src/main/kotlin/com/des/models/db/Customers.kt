package com.des.models.db

import com.des.models.CustomerDTO
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Customers : UUIDTable("Customers") {
    val firstName = varchar("firstName", 64)
    val lastName = varchar("lastName", 64)
    val email = varchar("email", 64)
    val username = varchar("username", 64)
}

class Customer(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Customer>(Customers)

    var firstName by Customers.firstName
    var lastName by Customers.lastName
    var email by Customers.email
    var username by Customers.username

    val orders by Order referrersOn Orders.customer
}

fun Customer.toDTO() = CustomerDTO(
    //id = id.toString(),
    firstName = firstName,
    lastName = lastName,
    email = email,
    username = username,
    uuid = id.toString(),
)