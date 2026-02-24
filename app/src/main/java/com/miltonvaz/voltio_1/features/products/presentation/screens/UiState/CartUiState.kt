package com.miltonvaz.voltio_1.features.products.presentation.screens.UiState

import com.miltonvaz.voltio_1.features.products.domain.entities.Product

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class CartItem(
    val product: Product,
    val quantity: Int = 1
)
