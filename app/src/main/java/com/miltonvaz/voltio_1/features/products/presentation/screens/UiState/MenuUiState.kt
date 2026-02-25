package com.miltonvaz.voltio_1.features.products.presentation.screens.UiState

import com.miltonvaz.voltio_1.features.products.domain.entities.Product

data class MenuUiState(
    val products: List<Product> = emptyList(),
    val totalOrdersToday: Int = 0,
    val totalStockValue: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)
