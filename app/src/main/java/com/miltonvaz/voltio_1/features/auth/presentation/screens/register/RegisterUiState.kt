package com.miltonvaz.voltio_1.features.auth.presentation.screens.register

import com.miltonvaz.voltio_1.features.auth.domain.entities.Auth

data class RegisterUiState(
    val isLoading: Boolean = false,
    val user: Auth? = null,
    val isSuccess: Boolean = false,
    val error: String? = null
)