package com.des.models

import kotlinx.serialization.Serializable

@Serializable
data class OrderDTO(val customerUserName: String, val items: List<ItemDTO> = listOf(), val id: Int? = null, val orderNumber: Int? = null)

@Serializable
data class ItemDTO(val productId: String, val amount: Int)
