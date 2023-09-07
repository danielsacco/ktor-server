package com.des.plugins

import com.des.routes.customerRouting
import com.des.routes.orderRouting
import com.des.routes.productRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        customerRouting()
        productRouting()
        orderRouting()
    }
}
