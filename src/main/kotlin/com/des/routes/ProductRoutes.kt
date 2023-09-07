package com.des.routes

import com.des.dao.productDAO
import com.des.models.ProductDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.productRouting() {
    route("/product") {
        get {
            val products = productDAO.products()

            if (products.isNotEmpty()) {
                call.respond(products)
            } else {
                call.respondText("No products found", status = HttpStatusCode.OK)
            }
        }
        post {
            val product = this.call.receive<ProductDTO>()
            call.respond(productDAO.createProduct(product))
        }

    }

}