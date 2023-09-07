package com.des.dao

import com.des.models.CustomerDTO
import com.des.models.OrderDTO

interface CustomerDAO {

    suspend fun customers(): List<CustomerDTO>

    suspend fun findCustomerByUsername(username: String): CustomerDTO?

    suspend fun createCustomer(customer: CustomerDTO): CustomerDTO?

    suspend fun deleteCustomer(username: String): Boolean

    suspend fun customerOrders(customerId: String): List<OrderDTO>
}

