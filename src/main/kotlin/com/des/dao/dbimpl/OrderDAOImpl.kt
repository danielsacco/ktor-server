package com.des.dao.dbimpl

import com.des.dao.DatabaseFactoryImpl.dbQuery
import com.des.dao.OrderDAO
import com.des.models.OrderDTO
import com.des.models.db.*
import java.time.LocalDateTime

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

    override suspend fun addItem(orderId: String, productId: String, amount: Int): OrderDTO? = dbQuery {
        //var item: Item? = null
        Order.findById(orderId.toInt())?.let {orderEntity ->

            Product.findById(productId.toInt())?.let { productEntity ->
                //item =
                    Item.new {
                    order = orderEntity
                    product = productEntity
                    quantity = amount
                }
            }
        }
        //item?.toDTO()
        Order.findById(orderId.toInt())?.toDTO()
    }

}