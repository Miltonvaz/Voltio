package com.miltonvaz.voltio_1.features.products.presentation.screens.UiState

import com.miltonvaz.voltio_1.features.products.domain.entities.Product

data class ProductFormUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val currentProduct: Product? = null
)