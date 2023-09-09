package com.des.models

import com.des.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Customer(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
)
