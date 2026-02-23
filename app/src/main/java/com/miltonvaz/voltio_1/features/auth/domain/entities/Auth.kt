package com.miltonvaz.voltio_1.features.auth.domain.entities

data class Auth(
    val id: Int,
    val name: String,
    val secondname: String?,
    val lastname: String,
    val secondlastname: String?,
    val email: String,
    val phone: String?,
    val image_profile: String?,
    val role: String,
    val created_at: String
)