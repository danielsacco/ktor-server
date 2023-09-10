package com.des.routes

import com.des.daos.ProductDAO
import com.des.models.Product
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.productRouting() {
    val productDao by inject<ProductDAO>()
    val defaultPageSize = environment?.config?.propertyOrNull("pagination.pageSize")
        ?.getString()?.toIntOrNull() ?: 20
    val maxPageSize = environment?.config?.propertyOrNull("pagination.maxPageSize")
        ?.getString()?.toIntOrNull() ?: 100

    route("/products") {
        get {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize = minOf(
                call.request.queryParameters["pageSize"]?.toIntOrNull() ?: defaultPageSize,
                maxPageSize
            )
            val products = productDao.products(page = page, pageSize = pageSize)
            call.respond(products)
        }
        post {
            val product = this.call.receive<Product>()
            call.respond(message = productDao.createProduct(product), status = HttpStatusCode.Created)
        }

    }
}