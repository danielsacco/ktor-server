package com.des.dao

import com.des.models.ProductDTO
import java.util.*

interface ProductDAO {

    suspend fun createProduct(product: ProductDTO): ProductDTO

    suspend fun products(): List<ProductDTO>

    suspend fun findProduct(id: Int) : ProductDTO?
}