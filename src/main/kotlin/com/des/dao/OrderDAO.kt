package com.des.dao

import com.des.models.Order

interface OrderDAO {

    suspend fun orders() : List<Order>

    suspend fun createOrder(order: Order) : Order

    suspend fun addItem(orderId: Int, productId: Int, amount: Int): Order?

    suspend fun findOrder(orderId: Int): Order?

}