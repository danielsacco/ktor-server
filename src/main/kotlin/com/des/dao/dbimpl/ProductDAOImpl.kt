package com.des.dao.dbimpl

import com.des.dao.DatabaseFactory
import com.des.dao.ProductDAO
import com.des.models.Product
import com.des.models.db.ProductEntity

class ProductDAOImpl(private val databaseFactory: DatabaseFactory) : ProductDAO {

    override suspend fun createProduct(product: Product): Product = databaseFactory.dbQuery {
        val newProductEntity = ProductEntity.new {
            name = product.name
            price = product.price
            description = product.description
        }
        newProductEntity.toProduct()
    }

    override suspend fun products(): List<Product> = databaseFactory.dbQuery {
        ProductEntity.all().map { it.toProduct() }
    }

    override suspend fun findProduct(id: Int) : Product? = databaseFactory.dbQuery {
        ProductEntity.findById(id)?.toProduct()
    }
}

fun ProductEntity.toProduct() = Product(
    price = price,
    name = name,
    description = description,
    id = id.value
)