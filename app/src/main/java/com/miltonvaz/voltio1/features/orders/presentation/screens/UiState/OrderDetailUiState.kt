package com.miltonvaz.voltio1.features.orders.presentation.screens.UiState

import com.miltonvaz.voltio1.features.orders.domain.entities.Order

data class OrderDetailUiState(
    val order: Order? = null,
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val error: String? = null,
    val isAssigning: Boolean = false
)
