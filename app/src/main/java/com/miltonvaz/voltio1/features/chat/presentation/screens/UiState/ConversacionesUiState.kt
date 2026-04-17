package com.miltonvaz.voltio1.features.chat.presentation.screens.UiState

import com.miltonvaz.voltio1.features.chat.domain.entities.Conversacion

data class ConversacionesUiState(
    val isLoading: Boolean = false,
    val conversaciones: List<Conversacion> = emptyList(),
    val filtradas: List<Conversacion> = emptyList(),
    val query: String = "",
    val error: String? = null
)