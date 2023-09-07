package com.des.routes

import com.des.dao.OrderDAO
import com.des.models.ItemDTO
import com.des.models.OrderDTO
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
            val order = this.call.receive<OrderDTO>()
            //ordersDAO.createOrder(order)
            call.respond(ordersDao.createOrder(order))
        }
        put("/{orderId}/item") {
            val orderId = call.parameters["orderId"] ?: return@put call.respondText(
                text ="Missing order ID",
                status = HttpStatusCode.BadRequest)
            val item = call.receive<ItemDTO>()
            val order = ordersDao.addItem(orderId = orderId, productId = item.productId, amount = item.amount)
                ?: return@put call.respondText(
                    text ="Order or Product not found",
                    status = HttpStatusCode.BadRequest)
            call.respond(order)
        }
    }

}

//fun Route.getOrderRoute() {
//    get("/order/{id?}") {
//        val id = call.parameters["id"] ?: return@get call.respondText(
//            text ="Bad Request",
//            status = HttpStatusCode.BadRequest)
//        val order = orderStorage.find { it.orderId == id } ?: return@get call.respondText(
//            text = "Order not found",
//            status = HttpStatusCode.NotFound)
//        call.respond(order)
//    }
//}

// TODO No borrar esta logica
//fun Route.totalizeOrderRoute() {
//    get("/order/{id?}/total") {
//        val id = call.parameters["id"] ?: return@get call.respondText(
//            text ="Bad Request",
//            status = HttpStatusCode.BadRequest)
//        val order = orderStorage.find { it.orderId == id } ?: return@get call.respondText(
//            text = "Order not found",
//            status = HttpStatusCode.NotFound)
//        val total = order.contents.sumOf { it.amount * it.price }
//        call.respond(total)
//    }
//}