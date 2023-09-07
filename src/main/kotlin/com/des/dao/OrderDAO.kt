package com.des.dao

import com.des.models.OrderDTO

interface OrderDAO {

    suspend fun orders() : List<OrderDTO>

    suspend fun createOrder(order: OrderDTO) : OrderDTO

    suspend fun addItem(orderId: String, productId: String, amount: Int): OrderDTO?

}