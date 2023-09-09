package com.des.models

import com.des.serializers.UUIDSerializer
import com.des.serializers.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.*

@Serializable
class Product(
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,
    val name: String,
    val description: String,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
)