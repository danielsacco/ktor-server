package com.des.dao.dbimpl

import com.des.dao.DatabaseFactory
import com.des.dao.OrderDAO
import com.des.models.OrderDTO
import com.des.models.db.*
import java.time.LocalDateTime

class OrderDAOImpl(private val databaseFactory: DatabaseFactory) : OrderDAO {

    override suspend fun orders(): List<OrderDTO> = databaseFactory.dbQuery {
        Order.all().map { it.toDTO() }
    }

    override suspend fun createOrder(order: OrderDTO): OrderDTO = databaseFactory.dbQuery {
        val customer = Customer.find {
            Customers.username eq order.customerUserName
        }.single()    // TODO Fix this in case the customer does not exist or is duplicated

        val newOrder = Order.new {
            this.customer = customer
            orderDate = LocalDateTime.now()
        }
        newOrder.refresh(true)
        newOrder.toDTO()
    }

    override suspend fun addItem(orderId: Int, productId: Int, amount: Int): OrderDTO? = databaseFactory.dbQuery {
        Order.findById(orderId)?.let {orderEntity ->

            Product.findById(productId.toInt())?.let { productEntity ->
                    Item.new {
                    order = orderEntity
                    product = productEntity
                    quantity = amount
                }
            }
        }
        Order.findById(orderId)?.toDTO()
    }

    override suspend fun findOrder(orderId: Int): OrderDTO? = databaseFactory.dbQuery {
        Order.findById(orderId)?.toDTO()
    }
}