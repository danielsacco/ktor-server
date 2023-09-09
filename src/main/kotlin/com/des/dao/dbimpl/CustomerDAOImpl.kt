package com.des.dao.dbimpl

import com.des.dao.CustomerDAO
import com.des.dao.DatabaseFactory
import com.des.models.Customer
import com.des.models.Order
import com.des.models.db.CustomerEntity
import com.des.models.db.CustomersTable

class CustomerDAOImpl(private val databaseFactory: DatabaseFactory) : CustomerDAO {

    override suspend fun customers(): List<Customer> = databaseFactory.dbQuery {
        CustomerEntity.all().map { it.toCustomer() }
    }
    
    override suspend fun findCustomerByUsername(username: String): Customer? = databaseFactory.dbQuery {
        CustomerEntity.find { CustomersTable.username eq username }
            .singleOrNull()?.toCustomer()
    }
    
    override suspend fun createCustomer(customer: Customer): Customer? = databaseFactory.dbQuery {
        // TODO username should be unique
        //Customer.find { Customers.username eq customer.username }

        val newCustomerEntity = CustomerEntity.new {
            firstName = customer.firstName
            lastName = customer.lastName
            email = customer.email
            username = customer.username
        }
        newCustomerEntity.toCustomer()
    }

    override suspend fun deleteCustomer(username: String): Boolean = databaseFactory.dbQuery {
        // TODO Should check whether the customer has opened orders
        val customerEntity = CustomerEntity.find { CustomersTable.username eq username }.singleOrNull()

        if(customerEntity != null) {
            customerEntity.delete()
            true
        } else {
            false
        }

    }

    override suspend fun customerOrders(customerId: String): List<Order> = databaseFactory.dbQuery {
        CustomerEntity.findById(customerId.toInt())
            ?.orders
            ?.map { it.toOrder() }
            ?: listOf()
    }
}

fun CustomerEntity.toCustomer() = Customer(
    firstName = firstName,
    lastName = lastName,
    email = email,
    username = username,
    id = id.value,
)