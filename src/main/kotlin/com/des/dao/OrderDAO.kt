package com.des.dao

import com.des.models.OrderDTO

interface OrderDAO {

    suspend fun orders() : List<OrderDTO>

    suspend fun createOrder(order: OrderDTO) : OrderDTO

    suspend fun addItem(orderId: Int, productId: Int, amount: Int): OrderDTO?

    suspend fun findOrder(orderId: Int): OrderDTO?

}