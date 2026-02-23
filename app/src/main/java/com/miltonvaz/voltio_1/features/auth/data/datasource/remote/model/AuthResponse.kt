package com.ameth.voltio.features.login.data.datasource.remote.model


import com.miltonvaz.voltio_1.features.auth.domain.entities.Auth

data class AuthResponse(
    val message: String,
    val accessToken: String,
    val refreshToken: String,
    val user: Auth
)

