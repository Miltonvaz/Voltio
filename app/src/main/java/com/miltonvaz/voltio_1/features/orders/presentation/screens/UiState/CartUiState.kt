package com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState

import com.miltonvaz.voltio_1.features.orders.domain.entities.CartItem

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
