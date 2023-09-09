package com.des.daos.dbimpl

import com.des.daos.DatabaseFactoryUnitTest
import com.des.daos.ProductDAO
import com.des.models.Product
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
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProductDAOImplTest : KoinTest {

    private lateinit var databaseFactory: DatabaseFactoryUnitTest

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules( module {
            single<ProductDAO> { ProductDAOImpl(databaseFactory) }
        })
    }

    private val dao : ProductDAO by inject()

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
    fun `Given an empty repository When querying for products Then an empty list is returned`() = runTest {
        val products = dao.products()
        assertTrue(products.isEmpty())
    }

    @Test
    fun `Given a single product is added When queried all products Then that product is the only returned`() = runTest {
        dao.createProduct(testProduct)

        val products = dao.products()

        assertEquals(1, products.size)
        assertEquals(testProduct.name, products[0].name)
    }

    @Test
    fun `Given an existing product When queried by id Then that product is returned`() = runTest {
        val id = dao.createProduct(testProduct).id

        val product = dao.findProduct(id!!)

        assertNotNull(product)
        assertEquals(testProduct.name, product.name)
        assertEquals(testProduct.description, product.description)
        assertEquals(testProduct.price, product.price)
    }


    companion object {
        const val NAME = "Batata x Kg."
        const val DESCRIPTION = "Para engordar"
        val testPrice = BigDecimal("12.21")

        val testProduct = Product(
            name = NAME,
            description = DESCRIPTION,
            price = testPrice
        )
    }
}