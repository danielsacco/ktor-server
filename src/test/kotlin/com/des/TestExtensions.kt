package com.des

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*

/**
 * Simple extension to create a Json aware httpclient just to reduce boilerplate on each test.
 */
fun ApplicationTestBuilder.createClientWithJson() = createClient {
    install(ContentNegotiation) {
        json()
    }
}