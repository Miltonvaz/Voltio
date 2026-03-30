package com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model

data class RegisterFCMTokenRequest(
    val userId: Int,
    val token: String
)
