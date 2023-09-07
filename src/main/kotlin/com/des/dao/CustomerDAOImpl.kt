package com.des.dao

import com.des.dao.DatabaseFactory.dbQuery
import com.des.models.CustomerDTO
import com.des.models.db.Customer
import com.des.models.db.Customers
import com.des.models.db.toDTO


class CustomerDAOImpl : CustomerDAO {

    override suspend fun customers(): List<CustomerDTO> = dbQuery {
        Customer.all().map { it.toDTO() }
    }
    
    override suspend fun findCustomerByUsername(username: String): CustomerDTO? = dbQuery {
        Customer.find { Customers.username eq username }
            .singleOrNull()?.toDTO()
    }
    
    override suspend fun createCustomer(customer: CustomerDTO): CustomerDTO? = dbQuery {
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

    override suspend fun deleteCustomer(username: String): Boolean = dbQuery {
        // TODO Should check whether the customer has opened orders
        val customer = Customer.find { Customers.username eq username }.singleOrNull()

        if(customer != null) {
            customer.delete()
            true
        } else {
            false
        }

    }
}

// dao Factory and initializer. TODO: Should be in a configuration file
//val customerDao: CustomerDAO = CustomerDAOImpl().apply {
//    runBlocking {
//        if(customers().isEmpty()) {
//            createCustomer(CustomerDTO(username = "johndoe", firstName = "John", lastName = "Doe", email = "john.doe@mail.com"))
//            createCustomer(CustomerDTO(username = "danielsacco", firstName = "Daniel", lastName = "Sacco", email = "daniel.sacco@mail.com"))
//            createCustomer(CustomerDTO(username = "flea", firstName = "Michael Peter", lastName = "Balzary", email = "flea.thebest@mail.com"))
//        }
//    }
//}