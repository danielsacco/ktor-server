package com.des.routes

import com.des.daos.CustomerDAO
import com.des.models.Customer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.customerRouting() {

    val customerDao by inject<CustomerDAO>()
    val defaultPageSize = environment?.config?.propertyOrNull("pagination.pageSize")
        ?.getString()?.toIntOrNull() ?: 20
    val maxPageSize = environment?.config?.propertyOrNull("pagination.maxPageSize")
        ?.getString()?.toIntOrNull() ?: 100

    route("/customers") {
        get {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize = minOf(
                call.request.queryParameters["pageSize"]?.toIntOrNull() ?: defaultPageSize,
                maxPageSize
            )
            call.respond(customerDao.customers(page = page, pageSize = pageSize))
        }
        get("{username?}") {
            val username = call.parameters["username"] ?: return@get call.respondText(
                "Missing username",
                status = HttpStatusCode.BadRequest
            )
            val customer = customerDao.findCustomerByUsername(username) ?: return@get call.respondText(
                "No customer with username $username",
                status = HttpStatusCode.NotFound
            )
            call.respond(customer)
        }
        get("/{customerId}/orders") {
            val customerId = call.parameters["customerId"] ?: return@get call.respondText(
                text ="Missing customer ID",
                status = HttpStatusCode.BadRequest)
            call.respond(customerDao.customerOrders(customerId))
        }
        post {
            val customer = this.call.receive<Customer>()
            val createdCustomer = customerDao.createCustomer(customer)
            call.respond(HttpStatusCode.Created, createdCustomer!!)     // TODO What if not created ?
        }
        delete("{username?}") {
            val username = call.parameters["username"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (customerDao.deleteCustomer(username)) {
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}