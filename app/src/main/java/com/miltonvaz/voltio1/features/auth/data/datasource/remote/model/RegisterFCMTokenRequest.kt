package com.miltonvaz.voltio1.features.auth.data.datasource.remote.model

data class RegisterFCMTokenRequest(
    val userId: Int,
    val token: String
)
