package com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model


data class UserDto(
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

data class ProfileResponse(
    val user: UserDto
)

data class MessageResponse(
    val message: String
)