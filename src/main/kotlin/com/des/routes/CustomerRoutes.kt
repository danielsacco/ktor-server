package com.des.routes

import com.des.dao.customerDao
import com.des.models.CustomerDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.customerRouting() {
    route("/customer") {
        get {
            val customers = customerDao.customers()
            
            if (customers.isNotEmpty()) {
                call.respond(customers)
            } else {
                call.respondText("No customers found", status = HttpStatusCode.OK)
            }
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
        post {
            val customer = this.call.receive<CustomerDTO>()
            customerDao.createCustomer(customer)
            call.respond(customer)
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