package com.des.routes

import com.des.createClientWithJson
import com.des.models.Customer
import com.des.models.Order
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
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
        val response = client.get("/customers")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `When posting a customer Then it is created`() = testApplication {
        val client = createClientWithJson()
        val response = client.post("/customers") {
            contentType(ContentType.Application.Json)
            setBody(testCustomer)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val result = response.body() as Customer
        assertEquals(testCustomer.username, result.username)
        assertEquals(testCustomer.firstName, result.firstName)
        assertEquals(testCustomer.lastName, result.lastName)
        assertEquals(testCustomer.email, result.email)
    }

    @Test
    fun `When querying an existing customer by name Then it is found`() = testApplication {
        val client = createClientWithJson()
        client.post("/customers") {
            contentType(ContentType.Application.Json)
            setBody(testCustomer)
        }

        val response = client.get("/customers/user")
        assertEquals(HttpStatusCode.OK, response.status)
        val result = response.body() as Customer
        assertEquals(testCustomer.username, result.username)
        assertEquals(testCustomer.firstName, result.firstName)
        assertEquals(testCustomer.lastName, result.lastName)
        assertEquals(testCustomer.email, result.email)
    }

    @Test
    fun `When querying a non-existing customer by name Then an error is returned`() = testApplication {
        val client = createClientWithJson()

        val response = client.get("/customers/non-existing-user")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals(
            "No customer with username non-existing-user",
            response.bodyAsText()
        )
    }

    @Test
    fun `When deleting an existing customer by name Then it is removed`() = testApplication {
        val client = createClientWithJson()
        val creationResponse = client.post("/customers") {
            contentType(ContentType.Application.Json)
            setBody(testCustomer)
        }
        assertEquals(HttpStatusCode.Created, creationResponse.status)

        val response = client.delete("/customers/${testCustomer.username}")
        assertEquals(HttpStatusCode.Accepted, response.status)
    }

    @Test
    fun `When querying orders of an existing customer by name Then they are retrieved`() = testApplication {
        val client = createClientWithJson()
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

        val customer = customerCreationResponse.body() as Customer
        val response = client.get("/customers/${customer.id}/orders")
        val orders = response.body() as List<Order>
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(1, orders.size)
    }


    companion object {
        val testCustomer = Customer(username = "user", firstName = "name", lastName = "surname", email = "email")
    }
}