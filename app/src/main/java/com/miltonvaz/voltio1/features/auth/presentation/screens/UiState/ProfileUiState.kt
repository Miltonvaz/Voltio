package com.miltonvaz.voltio1.features.auth.presentation.screens.UiState

import com.miltonvaz.voltio1.features.auth.data.datasource.remote.model.UserDto

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserDto? = null,
    val error: String? = null
)
