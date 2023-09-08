package com.des.dao.dbimpl

import com.des.dao.CustomerDAO
import com.des.dao.DatabaseFactoryUnitTest
import com.des.dao.OrderDAO
import com.des.models.CustomerDTO
import com.des.models.OrderDTO
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.component.inject
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import kotlin.test.assertEquals

class CustomerDAOImplTest : KoinTest {

    private lateinit var databaseFactory: DatabaseFactoryUnitTest

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules( module {
            single<CustomerDAO> { CustomerDAOImpl(databaseFactory) }
            //single<ProductDAO> { ProductDAOImpl(databaseFactory) }
            single<OrderDAO> { OrderDAOImpl(databaseFactory) }
        })
    }

    private val customerDao : CustomerDAO by inject()
    //private val productDao : ProductDAO by inject()
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
    fun `Given an empty repository When querying for customers Then an empty list is returned`() = runTest {
        val customers = customerDao.customers()
        assertTrue(customers.isEmpty())
    }

    @Test
    fun `Given a single customer is added When queried all customers Then that customer is the only returned`() = runTest {
        customerDao.createCustomer(testCustomer)

        val customers = customerDao.customers()

        assertEquals(1, customers.size)
        assertEquals(testCustomer.username, customers[0].username)
    }

    @Test
    fun `Given an existing customer When queried by id Then that customer is returned`() = runTest {
        customerDao.createCustomer(testCustomer)

        val customer = customerDao.findCustomerByUsername(testCustomer.username)

        assertNotNull(customer)
        assertEquals(testCustomer.username, customer?.username)
        assertEquals(testCustomer.firstName, customer?.firstName)
        assertEquals(testCustomer.lastName, customer?.lastName)
        assertEquals(testCustomer.email, customer?.email)
    }

    @Test
    fun `Given an existing customer When deleted Then it no longer exists`() = runTest {
        customerDao.createCustomer(testCustomer)
        assertNotNull(customerDao.findCustomerByUsername(testCustomer.username))

        val deleteResult = customerDao.deleteCustomer(username = testCustomer.username)
        assertTrue(deleteResult)

        val customer = customerDao.findCustomerByUsername(testCustomer.username)

        assertNull(customer)
    }

    @Test
    fun `Given an un-existing customer When deleted Then the call returns false`() = runTest {
        val deleteResult = customerDao.deleteCustomer(username = USERNAME)
        assertFalse(deleteResult)
    }

    @Test
    fun `Given an existing order for a customer When queried Then it returns the order into the response`() = runTest {
        customerDao.createCustomer(testCustomer)
        orderDao.createOrder(
            OrderDTO(
                customerUserName = testCustomer.username,
            )
        )

        val customer = customerDao.findCustomerByUsername(testCustomer.username)
        val orders = customerDao.customerOrders(customer!!.id.toString())

        assertEquals(1, orders.size)
    }

    companion object {
        const val USERNAME = "johndoe"
        const val FIRSTNAME = "John"
        const val LASTNAME = "Doe"
        const val EMAIL = "john.doe@mail.com"

        val testCustomer = CustomerDTO(
            username = USERNAME,
            firstName = FIRSTNAME,
            lastName = LASTNAME,
            email = EMAIL
        )
    }
}