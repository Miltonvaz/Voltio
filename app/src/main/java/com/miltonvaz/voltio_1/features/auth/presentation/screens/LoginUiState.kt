package com.miltonvaz.voltio_1.features.auth.presentation.screens

import com.miltonvaz.voltio_1.features.auth.domain.entities.Auth

data class LoginUiState(
    val isLoading: Boolean = false,
    val user: Auth? = null,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)