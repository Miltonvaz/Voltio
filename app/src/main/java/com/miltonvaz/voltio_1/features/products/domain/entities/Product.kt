package com.miltonvaz.voltio_1.features.products.domain.entities

data class Product(
    val id: Int,
    val sku: String,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val imageUrl: String?,
    val categoryId: Int?,
    val registerDate: String,
    val specifications: List<Specification> = emptyList()
)

