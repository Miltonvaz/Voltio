package com.miltonvaz.voltio1.features.auth.data.datasource.remote.model

data class GoogleAuthRequest(
    val idToken: String,
    val role: String? = "user",
    val commercialName: String? = null,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val vehicle: String? = null,
    val plates: String? = null
)
