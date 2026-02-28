package com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState

import com.miltonvaz.voltio_1.features.orders.domain.entities.Order

data class OrdersUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
