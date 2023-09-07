package com.des.models.db

import com.des.models.CustomerDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Customers : IntIdTable("Customers") {
    val firstName = varchar("firstName", 64)
    val lastName = varchar("lastName", 64)
    val email = varchar("email", 64)
    val username = varchar("username", 64)
}

class Customer(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Customer>(Customers)

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
    id = id.value,
)