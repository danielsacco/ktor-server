package com.des.routes

import com.des.models.CustomerDTO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
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
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `When queried for customers Then am empty list is obtained`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val response = client.get("/customer")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `When posting a customer Then it is created`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/customer") {
            contentType(ContentType.Application.Json)
            setBody(CustomerDTO("user", "name", "surname", "email"))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(
            """{"username":"user","firstName":"name","lastName":"surname","email":"email","id":1}""",
            response.bodyAsText()
        )
    }

    @Test
    fun `When querying an existing customer by name Then it is found`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.post("/customer") {
            contentType(ContentType.Application.Json)
            setBody(CustomerDTO("user", "name", "surname", "email"))
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
        environment {
            config = ApplicationConfig("application-test.conf")
        }
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

}