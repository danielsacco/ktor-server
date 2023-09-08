package com.des.dao.dbimpl

import com.des.dao.CustomerDAO
import com.des.dao.DatabaseFactoryUnitTest
import com.des.dao.OrderDAO
import com.des.dao.ProductDAO
import com.des.dao.dbimpl.CustomerDAOImplTest.Companion.testCustomer
import com.des.dao.dbimpl.ProductDAOImplTest.Companion.testProduct
import com.des.models.OrderDTO
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.assertTrue
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
            OrderDTO(
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