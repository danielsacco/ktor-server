package com.des.models

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val id: Int? = null,
)
