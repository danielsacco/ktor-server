package com.des.models

import com.des.serializer.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
class ProductDTO(
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,
    val name: String,
    val description: String,
    val id: Int? = null)