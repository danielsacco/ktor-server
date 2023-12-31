package com.des.daos

import com.des.models.Order
import java.util.*

interface OrderDAO {

    suspend fun orders(page: Int = 1, pageSize: Int = 100) : List<Order>

    suspend fun createOrder(order: Order) : Order

    suspend fun addItem(orderId: UUID, productId: UUID, amount: Int): Order?

    suspend fun findOrder(orderId: UUID): Order?

}