package com.des.plugins

import com.des.routes.customerRouting
import com.des.routes.orderRouting
import com.des.routes.productRouting
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        customerRouting()
//        listOrdersRoute()
//        getOrderRoute()
//        totalizeOrderRoute()
        productRouting()
        orderRouting()

        staticResources(
            remotePath = "/static",
            basePackage = "files"
        )
    }
}
