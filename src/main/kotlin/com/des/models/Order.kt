package com.des.models

import com.des.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Order(
    val customerUserName: String,
    val items: List<Item> = listOf(),
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val orderNumber: Int? = null
)

@Serializable
data class Item(
    val product: Product,
    val amount: Int,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null
)

@Serializable
data class ItemAdd(
    @Serializable(with = UUIDSerializer::class)
    val productId: UUID,
    val amount: Int
)