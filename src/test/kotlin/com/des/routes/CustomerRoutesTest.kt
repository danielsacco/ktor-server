package com.des.routes

import com.des.models.CustomerDTO
import com.des.models.OrderDTO
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

class CustomerRoutesTest {

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `When queried for root content Then NotFound is obtained`() = testApplication {
        val response = client.get("/")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `When queried for customers Then am empty list is obtained`() = testApplication {
        val response = client.get("/customer")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `When posting a customer Then it is created`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/customer") {
            contentType(ContentType.Application.Json)
            setBody(testCustomer)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(
            """{"username":"user","firstName":"name","lastName":"surname","email":"email","id":1}""",
            response.bodyAsText()
        )
    }

    @Test
    fun `When querying an existing customer by name Then it is found`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.post("/customer") {
            contentType(ContentType.Application.Json)
            setBody(testCustomer)
        }

        val response = client.get("/customer/user")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            """{"username":"user","firstName":"name","lastName":"surname","email":"email","id":1}""",
            response.bodyAsText()
        )
    }

    @Test
    fun `When querying a non-existing customer by name Then an error is returned`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/customer/non-existing-user")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals(
            "No customer with username non-existing-user",
            response.bodyAsText()
        )
    }

    @Test
    fun `When deleting an existing customer by name Then it is removed`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val creationResponse = client.post("/customer") {
            contentType(ContentType.Application.Json)
            setBody(testCustomer)
        }
        assertEquals(HttpStatusCode.Created, creationResponse.status)

        val response = client.delete("/customer/${testCustomer.username}")
        assertEquals(HttpStatusCode.Accepted, response.status)
    }

    @Test
    fun `When querying orders of an existing customer by name Then they are retrieved`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val customerCreationResponse = client.post("/customer") {
            contentType(ContentType.Application.Json)
            setBody(testCustomer)
        }
        assertEquals(HttpStatusCode.Created, customerCreationResponse.status)

        val orderCreationResponse = client.post("/order") {
            contentType(ContentType.Application.Json)
            setBody(OrderDTO(customerUserName = testCustomer.username))
        }
        assertEquals(HttpStatusCode.Created, orderCreationResponse.status)

        val customer = customerCreationResponse.body() as CustomerDTO
        val response = client.get("/customer/${customer.id}/orders")
        val orders = response.body() as List<OrderDTO>
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(1, orders.size)
    }


    companion object {
        val testCustomer = CustomerDTO(username = "user", firstName = "name", lastName = "surname", email = "email")
    }
}