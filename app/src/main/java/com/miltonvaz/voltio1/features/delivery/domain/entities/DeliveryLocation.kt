package com.miltonvaz.voltio1.features.delivery.domain.entities

data class DeliveryLocation(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)
