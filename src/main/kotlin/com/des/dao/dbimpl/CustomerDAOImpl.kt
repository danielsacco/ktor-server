package com.des.dao.dbimpl

import com.des.dao.CustomerDAO
import com.des.dao.DatabaseFactory
import com.des.models.CustomerDTO
import com.des.models.OrderDTO
import com.des.models.db.Customer
import com.des.models.db.Customers
import com.des.models.db.toDTO

class CustomerDAOImpl(private val databaseFactory: DatabaseFactory) : CustomerDAO {

    override suspend fun customers(): List<CustomerDTO> = databaseFactory.dbQuery {
        Customer.all().map { it.toDTO() }
    }
    
    override suspend fun findCustomerByUsername(username: String): CustomerDTO? = databaseFactory.dbQuery {
        Customer.find { Customers.username eq username }
            .singleOrNull()?.toDTO()
    }
    
    override suspend fun createCustomer(customer: CustomerDTO): CustomerDTO? = databaseFactory.dbQuery {
        // TODO username should be unique
        //Customer.find { Customers.username eq customer.username }

        val newCustomer = Customer.new {
            firstName = customer.firstName
            lastName = customer.lastName
            email = customer.email
            username = customer.username
        }
        newCustomer.toDTO()
    }

    override suspend fun deleteCustomer(username: String): Boolean = databaseFactory.dbQuery {
        // TODO Should check whether the customer has opened orders
        val customer = Customer.find { Customers.username eq username }.singleOrNull()

        if(customer != null) {
            customer.delete()
            true
        } else {
            false
        }

    }

    override suspend fun customerOrders(customerId: String): List<OrderDTO> = databaseFactory.dbQuery {
        Customer.findById(customerId.toInt())
            ?.orders
            ?.map { it.toDTO() }
            ?: listOf()
    }
}
