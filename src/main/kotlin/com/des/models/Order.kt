package com.des.models

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val customerUserName: String,
    val items: List<Item> = listOf(),
    val id: Int? = null,
    val orderNumber: Int? = null
)

@Serializable
data class Item(
    val product: Product,
    val amount: Int,
    val id: Int? = null
)

@Serializable
data class ItemAdd(
    val productId: Int,
    val amount: Int
)