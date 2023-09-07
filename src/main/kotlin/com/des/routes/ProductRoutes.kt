package com.des.routes

import com.des.dao.ProductDAO
import com.des.models.ProductDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.productRouting() {
    val productDao by inject<ProductDAO>()

    route("/product") {
        get {
            val products = productDao.products()

            if (products.isNotEmpty()) {
                call.respond(products)
            } else {
                call.respondText("No products found", status = HttpStatusCode.OK)
            }
        }
        post {
            val product = this.call.receive<ProductDTO>()
            call.respond(productDao.createProduct(product))
        }

    }

}