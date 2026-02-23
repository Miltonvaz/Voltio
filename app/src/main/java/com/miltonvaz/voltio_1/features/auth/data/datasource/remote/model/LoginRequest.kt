package com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model

data class LoginRequest(
    val email: String,
    val password: String
)