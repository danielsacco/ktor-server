package com.des.dao.dbimpl

import com.des.dao.DatabaseFactory
import com.des.dao.ProductDAO
import com.des.models.ProductDTO
import com.des.models.db.Product
import com.des.models.db.toDTO

class ProductDAOImpl(private val databaseFactory: DatabaseFactory) : ProductDAO {

    override suspend fun createProduct(product: ProductDTO): ProductDTO = databaseFactory.dbQuery {
        val newProduct = Product.new {
            name = product.name
            price = product.price
            description = product.description
        }
        newProduct.toDTO()
    }

    override suspend fun products(): List<ProductDTO> = databaseFactory.dbQuery {
        Product.all().map { it.toDTO() }
    }

    override suspend fun findProduct(id: Int) : ProductDTO? = databaseFactory.dbQuery {
        Product.findById(id)?.toDTO()
    }
}
