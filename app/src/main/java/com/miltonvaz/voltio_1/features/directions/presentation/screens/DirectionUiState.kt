package com.miltonvaz.voltio_1.features.directions.presentation.screens

import com.miltonvaz.voltio_1.features.directions.domain.entities.Direction

data class DirectionUiState(
    val isLoading: Boolean = false,
    val directions: List<Direction> = emptyList(),
    val error: String? = null,
    val isCreating: Boolean = false,
    val createSuccess: Boolean = false
)