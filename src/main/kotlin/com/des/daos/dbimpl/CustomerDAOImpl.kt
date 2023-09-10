package com.des.daos.dbimpl

import com.des.daos.CustomerDAO
import com.des.daos.DatabaseFactory
import com.des.models.Customer
import com.des.models.Order
import com.des.models.db.CustomerEntity
import com.des.models.db.CustomersTable
import com.des.models.db.ProductEntity
import java.util.*

class CustomerDAOImpl(private val databaseFactory: DatabaseFactory) : CustomerDAO {

    override suspend fun customers(page: Int, pageSize: Int): List<Customer> = databaseFactory.dbQuery {
        val offset = pageSize * (page - 1)
        CustomerEntity.all()
            .limit(pageSize, offset.toLong())
            .map { it.toCustomer() }
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
        CustomerEntity.findById(UUID.fromString(customerId))
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