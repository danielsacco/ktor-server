package com.des.models

import kotlinx.serialization.Serializable

@Serializable
data class CustomerDTO(val username: String, val firstName: String, val lastName: String, val email: String, val uuid: String? = null)
