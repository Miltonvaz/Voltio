package com.miltonvaz.voltio1.features.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio1.core.network.TokenManager
import com.miltonvaz.voltio1.features.chat.domain.usecase.GetConversacionesUseCase
import com.miltonvaz.voltio1.features.chat.domain.usecase.ObserveBadgeUpdateUseCase
import com.miltonvaz.voltio1.features.chat.presentation.screens.UiState.ConversacionesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversacionesViewModel @Inject constructor(
    private val getConversacionesUseCase: GetConversacionesUseCase,
    private val observeBadgeUpdateUseCase: ObserveBadgeUpdateUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConversacionesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarConversaciones()
        observarBadgeUpdates()
    }

    fun cargarConversaciones() {
        val idUsuario = tokenManager.getUserId() ?: return
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getConversacionesUseCase(idUsuario).fold(
                onSuccess = { lista ->
                    _uiState.update { it.copy(isLoading = false, conversaciones = lista, filtradas = lista) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { state ->
            val filtradas = if (query.isBlank()) state.conversaciones
            else state.conversaciones.filter {
                it.nombre_usuario.contains(query, ignoreCase = true)
            }
            state.copy(query = query, filtradas = filtradas)
        }
    }

    private fun observarBadgeUpdates() {
        viewModelScope.launch {
            observeBadgeUpdateUseCase().collect { (idConversacion, ultimoMensaje) ->
                _uiState.update { state ->
                    val updated = state.conversaciones.map { conv ->
                        if (conv.id_conversacion == idConversacion) {
                            conv.copy(
                                ultimo_mensaje = ultimoMensaje ?: conv.ultimo_mensaje,
                                no_leidos = conv.no_leidos + 1
                            )
                        } else conv
                    }
                    state.copy(
                        conversaciones = updated,
                        filtradas = if (state.query.isBlank()) updated
                        else updated.filter { it.nombre_usuario.contains(state.query, ignoreCase = true) }
                    )
                }
            }
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
}