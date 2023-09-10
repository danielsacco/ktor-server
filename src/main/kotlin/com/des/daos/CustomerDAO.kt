package com.des.daos

import com.des.models.Customer
import com.des.models.Order

interface CustomerDAO {

    suspend fun customers(page: Int = 1, pageSize: Int = 100): List<Customer>

    suspend fun findCustomerByUsername(username: String): Customer?

    suspend fun createCustomer(customer: Customer): Customer?

    suspend fun deleteCustomer(username: String): Boolean

    suspend fun customerOrders(customerId: String): List<Order>
}

