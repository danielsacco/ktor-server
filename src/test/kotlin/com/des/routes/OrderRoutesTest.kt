package com.des.routes

import com.des.models.ItemAdd
import com.des.models.Order
import com.des.models.Product
import com.des.routes.CustomerRoutesTest.Companion.testCustomer
import com.des.routes.ProductRoutesTest.Companion.testProduct
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.After
import org.junit.Test
import org.koin.core.context.stopKoin
import kotlin.test.assertEquals

class OrderRoutesTest {
    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `When queried for orders Then am empty list is obtained`() = testApplication {
        val response = client.get("/orders")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `When posting an order Then it is created`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val customerCreationResponse = client.post("/customers") {
            contentType(ContentType.Application.Json)
            setBody(testCustomer)
        }
        assertEquals(HttpStatusCode.Created, customerCreationResponse.status)

        val response = client.post("/orders") {
            contentType(ContentType.Application.Json)
            setBody(Order(customerUserName = testCustomer.username))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val result = response.body() as Order
        assertEquals(testCustomer.username, result.customerUserName)
        assertEquals(0, result.items.size)
    }

    @Test
    fun `When adding an item to an order Then it is created`() = testApplication {
        // GIVEN
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val productCreationResponse = client.post("/products") {
            contentType(ContentType.Application.Json)
            setBody(testProduct)
        }
        val customerCreationResponse = client.post("/customers") {
            contentType(ContentType.Application.Json)
            setBody(testCustomer)
        }
        assertEquals(HttpStatusCode.Created, customerCreationResponse.status)

        val orderCreationResponse = client.post("/orders") {
            contentType(ContentType.Application.Json)
            setBody(Order(customerUserName = testCustomer.username))
        }
        assertEquals(HttpStatusCode.Created, orderCreationResponse.status)

        val order = orderCreationResponse.body() as Order
        val product = productCreationResponse.body() as Product

        // WHEN
        val result = client.put("/orders/${order.id}/item") {
            contentType(ContentType.Application.Json)
            setBody(ItemAdd(productId = product.id!!, amount = 2))
        }
        val finalOrder = result.body() as Order

        // THEN
        assertEquals(1, finalOrder.items.size)
    }
}