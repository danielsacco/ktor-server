package com.des.routes

import com.des.daos.OrderDAO
import com.des.models.ItemAdd
import com.des.models.Order
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*


fun Route.orderRouting() {
    val ordersDao by inject<OrderDAO>()
    val defaultPageSize = environment?.config?.propertyOrNull("pagination.pageSize")
        ?.getString()?.toIntOrNull() ?: 20
    val maxPageSize = environment?.config?.propertyOrNull("pagination.maxPageSize")
        ?.getString()?.toIntOrNull() ?: 100

    route("/orders") {
        get {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize = minOf(
                call.request.queryParameters["pageSize"]?.toIntOrNull() ?: defaultPageSize,
                maxPageSize
            )
            call.respond(ordersDao.orders(page = page, pageSize = pageSize))
        }
        post {
            val order = this.call.receive<Order>()
            call.respond(status = HttpStatusCode.Created, message = ordersDao.createOrder(order))
        }
        put("/{orderId}/item") {
            val orderId = call.parameters["orderId"] ?: return@put call.respondText(
                text ="Missing order ID",
                status = HttpStatusCode.BadRequest)
            val item = call.receive<ItemAdd>()
            val order = ordersDao.addItem(orderId = UUID.fromString(orderId), productId = item.productId, amount = item.amount)
                ?: return@put call.respondText(
                    text ="Order or Product not found",
                    status = HttpStatusCode.BadRequest)
            call.respond(order)
        }
    }

}
