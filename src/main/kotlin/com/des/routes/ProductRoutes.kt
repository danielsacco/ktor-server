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
            call.respond(products)
        }
        post {
            val product = this.call.receive<ProductDTO>()
            call.respond(message = productDao.createProduct(product), status = HttpStatusCode.Created)
        }

    }

}