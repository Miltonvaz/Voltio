package com.miltonvaz.voltio_1.features.products.presentation.screens.UiState

import com.miltonvaz.voltio_1.features.products.domain.entities.Product

data class HomeUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)