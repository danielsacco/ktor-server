package com.des.routes

import com.des.dao.OrderDAO
import com.des.models.ItemAdd
import com.des.models.Order
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.orderRouting() {
    val ordersDao by inject<OrderDAO>()

    route("/order") {
        get {
            call.respond(ordersDao.orders())
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
            val order = ordersDao.addItem(orderId = orderId.toInt(), productId = item.productId, amount = item.amount)
                ?: return@put call.respondText(
                    text ="Order or Product not found",
                    status = HttpStatusCode.BadRequest)
            call.respond(order)
        }
    }

}
