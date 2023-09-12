package com.des.routes;

import com.des.createClientWithJson
import com.des.models.Product
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
import java.math.BigDecimal
import kotlin.test.assertEquals

class ProductRoutesTest {

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `When queried for products on an empty database Then am empty list is obtained`() = testApplication {
        val response = client.get("/products")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `When posting a product Then it is created`() = testApplication {
        val client = createClientWithJson()
        val response = client.post("/products") {
            contentType(ContentType.Application.Json)
            setBody(testProduct)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val result = response.body() as Product
        assertEquals(testProduct.description, result.description)
        assertEquals(testProduct.name, result.name)
        assertEquals(testProduct.price, result.price)
    }

    @Test
    fun `When queried for products Then the existing products are retrieved`() = testApplication {
        val client = createClientWithJson()
        client.post("/products") {
            contentType(ContentType.Application.Json)
            setBody(testProduct)
        }
        client.post("/products") {
            contentType(ContentType.Application.Json)
            setBody(anotherProduct)
        }

        val response = client.get("/products")
        assertEquals(HttpStatusCode.OK, response.status)
        val result = response.body() as List<Product>
        assertEquals(2, result.size)
    }

    companion object {
        val testProduct = Product(
            price = BigDecimal("22.11"),
            name = "Arroz x Kg.",
            description = "Cereal"
        )
        val anotherProduct = Product(
            price = BigDecimal("11.11"),
            name = "Papa x Kg.",
            description = "Tubérculo"
        )
    }

}
