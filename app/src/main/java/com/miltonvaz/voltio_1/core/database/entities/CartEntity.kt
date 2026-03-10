package com.miltonvaz.voltio_1.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey val productId: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String?,
    val sku: String
)
