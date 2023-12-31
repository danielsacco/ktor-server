package com.des.daos.dbimpl

import com.des.daos.CustomerDAO
import com.des.daos.DatabaseFactoryUnitTest
import com.des.daos.OrderDAO
import com.des.daos.ProductDAO
import com.des.daos.dbimpl.CustomerDAOImplTest.Companion.testCustomer
import com.des.daos.dbimpl.ProductDAOImplTest.Companion.testProduct
import com.des.models.Order
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.component.inject
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import kotlin.test.assertEquals

class OrderDAOImplTest : KoinTest {

    private lateinit var databaseFactory: DatabaseFactoryUnitTest

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules( module {
            single<CustomerDAO> { CustomerDAOImpl(databaseFactory) }
            single<ProductDAO> { ProductDAOImpl(databaseFactory) }
            single<OrderDAO> { OrderDAOImpl(databaseFactory) }
        })
    }

    private val customerDao : CustomerDAO by inject()
    private val productDao : ProductDAO by inject()
    private val orderDao : OrderDAO by inject()

    @Before
    fun setup() {
        databaseFactory = DatabaseFactoryUnitTest()
        databaseFactory.init()
    }

    @After
    fun tearDown() {
        databaseFactory.close()
    }

    @Test
    fun `Given an empty repository When querying for orders Then an empty list is returned`() = runTest {
        val orders = orderDao.orders()
        assertTrue(orders.isEmpty())
    }

    @Test
    fun `Given an existing order When adding an item Then the item is returned with the order`() = runTest {
        customerDao.createCustomer(testCustomer)
        val productId = productDao.createProduct(testProduct).id!!
        val order = orderDao.createOrder(
            Order(
                customerUserName = testCustomer.username,
            )
        )

        assertTrue(order.items.isEmpty())

        orderDao.addItem(
            orderId = order.id!!,
            productId = productId,
            amount = 2,
        )

        val result = orderDao.findOrder(order.id!!)
        assertEquals(1, result?.items?.size)
        assertEquals(productId, result?.items?.get(0)?.product?.id)
        assertEquals(2, result?.items?.get(0)?.amount)
    }


}