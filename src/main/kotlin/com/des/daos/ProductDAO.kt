package com.des.daos

import com.des.models.Product
import java.util.*

interface ProductDAO {

    suspend fun createProduct(product: Product): Product

    suspend fun products(): List<Product>

    suspend fun findProduct(id: UUID) : Product?
}