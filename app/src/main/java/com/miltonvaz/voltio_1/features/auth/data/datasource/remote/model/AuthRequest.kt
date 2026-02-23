package com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model

data class AuthRequest(
    val name: String,
    val secondname: String? = null,
    val lastname: String,
    val secondlastname: String? = null,
    val email: String,
    val password: String,
    val phone: String? = null,
    val image_profile: String? = null,
    val role: String? = "user"
)
