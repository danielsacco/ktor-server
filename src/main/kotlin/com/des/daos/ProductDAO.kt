package com.des.daos

import com.des.models.Product
import java.util.*

interface ProductDAO {

    suspend fun createProduct(product: Product): Product

    suspend fun products(page: Int = 1, pageSize: Int = 100): List<Product>

    suspend fun findProduct(id: UUID) : Product?
}