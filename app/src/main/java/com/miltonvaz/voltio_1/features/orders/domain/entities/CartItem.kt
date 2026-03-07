package com.miltonvaz.voltio_1.features.orders.domain.entities

import com.miltonvaz.voltio_1.features.products.domain.entities.Product

data class CartItem(
    val product: Product,
    val quantity: Int = 1
)
