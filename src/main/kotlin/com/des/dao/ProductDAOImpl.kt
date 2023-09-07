package com.des.dao

import com.des.dao.DatabaseFactory.dbQuery
import com.des.models.ProductDTO
import com.des.models.db.Product
import com.des.models.db.toDTO
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal

class ProductDAOImpl : ProductDAO {
    override suspend fun createProduct(product: ProductDTO): ProductDTO = dbQuery {
        val newProduct = Product.new {
            name = product.name
            price = product.price
            description = product.description
        }
        newProduct.toDTO()
    }

    override suspend fun products(): List<ProductDTO> = dbQuery {
        Product.all().map { it.toDTO() }
    }

    override suspend fun findProduct(id: Int) : ProductDTO? = dbQuery {
        Product.findById(id)?.toDTO()
    }
}

// dao Factory and initializer. TODO: Should be in a configuration file
val productDAO: ProductDAO = ProductDAOImpl().apply {
    runBlocking {
        if(products().isEmpty()) {
            createProduct(ProductDTO(name = "Papa", description = "Papa x Kg.", price = BigDecimal("123.45")))
            createProduct(ProductDTO(name = "Batata", description = "Batata x Kg.", price = BigDecimal("456.78")))
            createProduct(ProductDTO(name = "Queso Sardo", description = "Queso Sardo x Kg.", price = BigDecimal("9999.99")))
        }
    }
}