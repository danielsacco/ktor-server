package com.des.models

import kotlinx.serialization.Serializable
@Serializable
// TODO use BigDecimal for the price (needs custom serializer). Idem with UUID
class ProductDTO(val price: Double, val name: String, val description: String, val uuid: String? = null)