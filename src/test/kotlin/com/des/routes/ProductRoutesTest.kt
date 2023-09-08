package com.des.routes;

import com.des.models.ProductDTO
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
        val response = client.get("/product")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `When posting a product Then it is created`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/product") {
            contentType(ContentType.Application.Json)
            setBody(testProduct)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(
            """{"price":"22.11","name":"Arroz x Kg.","description":"Cereal","id":1}""",
            response.bodyAsText()
        )
    }

    @Test
    fun `When queried for products Then the existing products are retrieved`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.post("/product") {
            contentType(ContentType.Application.Json)
            setBody(testProduct)
        }
        client.post("/product") {
            contentType(ContentType.Application.Json)
            setBody(anotherProduct)
        }

        val response = client.get("/product")
        assertEquals(HttpStatusCode.OK, response.status)
        val result = response.body() as List<ProductDTO>
        assertEquals(2, result.size)
    }

    companion object {
        val testProduct = ProductDTO(
            price = BigDecimal("22.11"),
            name = "Arroz x Kg.",
            description = "Cereal"
        )
        val anotherProduct = ProductDTO(
            price = BigDecimal("11.11"),
            name = "Papa x Kg.",
            description = "Tubérculo"
        )
    }

}
