package com.miltonvaz.voltio1.features.auth.domain.entities

data class Company(
    val id: Int,
    val userId: Int,
    val commercialName: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val contactPhone: String,
    val contactEmail: String,
    val logoUrl: String?,
    val registrationDate: String,
    val totalProducts: Int = 0
)
