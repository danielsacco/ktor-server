package com.des.daos.dbimpl

import com.des.daos.DatabaseFactory
import com.des.daos.OrderDAO
import com.des.models.Item
import com.des.models.Order
import com.des.models.db.*
import java.time.LocalDateTime
import java.util.*

class OrderDAOImpl(private val databaseFactory: DatabaseFactory) : OrderDAO {

    override suspend fun orders(page: Int, pageSize: Int): List<Order> = databaseFactory.dbQuery {
        val offset = pageSize * (page - 1)
        OrderEntity.all()
            .limit(pageSize, offset.toLong())
            .map { it.toOrder() }
    }

    override suspend fun createOrder(order: Order): Order = databaseFactory.dbQuery {
        val customerEntity = CustomerEntity.find {
            CustomersTable.username eq order.customerUserName
        }.single()    // TODO Fix this in case the customer does not exist or is duplicated

        val newOrderEntity = OrderEntity.new {
            this.customerEntity = customerEntity
            orderDate = LocalDateTime.now()
        }
        newOrderEntity.refresh(true)
        newOrderEntity.toOrder()
    }

    override suspend fun addItem(orderId: UUID, productId: UUID, amount: Int): Order? = databaseFactory.dbQuery {
        OrderEntity.findById(orderId)?.let { orderEntity ->

            ProductEntity.findById(productId)?.let { productEntity ->
                    ItemEntity.new {
                    this.orderEntity = orderEntity
                    this.productEntity = productEntity
                    quantity = amount
                }
            }
        }
        OrderEntity.findById(orderId)?.toOrder()
    }

    override suspend fun findOrder(orderId: UUID): Order? = databaseFactory.dbQuery {
        OrderEntity.findById(orderId)?.toOrder()
    }
}

fun OrderEntity.toOrder(): Order = Order(
    id = id.value,
    orderNumber = orderNumber,
    items = items.map { it.toItem() },
    customerUserName = customerEntity.username,
)

fun ItemEntity.toItem() : Item = Item(
    product = this.productEntity.toProduct(),
    amount = this.quantity,
    id = this.id.value
)