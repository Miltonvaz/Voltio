package com.miltonvaz.voltio1.features.auth.domain.entities

data class DeliveryPersonInfo(
    val id: Int,
    val userId: Int,
    val vehicle: String,
    val plates: String?,
    val isActive: Boolean,
    val createdAt: String
)
