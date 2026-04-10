package com.miltonvaz.voltio1.features.auth.data.datasource.remote.model

data class DeliveryRegisterRequest(
    val name: String,
    val lastname: String,
    val email: String,
    val password: String,
    val phone: String,
    val vehicle: String, // 'moto' | 'auto' | 'bici'
    val plates: String?,
    val role: String = "delivery"
)
