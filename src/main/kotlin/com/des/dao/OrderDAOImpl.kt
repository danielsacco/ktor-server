package com.des.dao

import com.des.dao.DatabaseFactory.dbQuery
import com.des.models.OrderDTO
import com.des.models.db.*
import java.time.LocalDateTime
import java.util.*

class OrderDAOImpl : OrderDAO {


    override suspend fun orders(): List<OrderDTO> = dbQuery {
        Order.all().map { it.toDTO() }
    }

    override suspend fun createOrder(order: OrderDTO): OrderDTO = dbQuery {
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

//    suspend fun findOrder(orderId: String) = dbQuery {
//        // TODO
//    }

    override suspend fun addItem(orderId: String, productId: String, amount: Int): OrderDTO? = dbQuery {
        //var item: Item? = null
        Order.findById(UUID.fromString(orderId))?.let {orderEntity ->

            Product.findById(UUID.fromString(productId))?.let { productEntity ->
                //item =
                    Item.new {
                    order = orderEntity
                    product = productEntity
                    quantity = amount
                }
            }
        }
        //item?.toDTO()
        Order.findById(UUID.fromString(orderId))?.toDTO()
    }

}