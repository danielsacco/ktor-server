package com.des.dao

import com.des.models.Product

interface ProductDAO {

    suspend fun createProduct(product: Product): Product

    suspend fun products(): List<Product>

    suspend fun findProduct(id: Int) : Product?
}