package com.miltonvaz.voltio_1.features.directions.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.core.network.TokenManager
import com.miltonvaz.voltio_1.features.directions.data.datasource.remote.model.DirectionRequest
import com.miltonvaz.voltio_1.features.directions.domain.usecase.DirectionUseCase
import com.miltonvaz.voltio_1.features.directions.presentation.screens.DirectionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DirectionViewModel @Inject constructor(
    private val directionUseCase: DirectionUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DirectionUiState())
    val uiState = _uiState.asStateFlow()

    private val userId: Int get() = tokenManager.getUserId()

    init {
        loadDirections()
    }

    fun loadDirections() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            directionUseCase.getByUserId(userId).fold(
                onSuccess = { directions ->
                    _uiState.update { it.copy(isLoading = false, directions = directions) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    fun createDirection(request: DirectionRequest) {
        _uiState.update { it.copy(isCreating = true, error = null) }
        viewModelScope.launch {
            directionUseCase.create(request.copy(id_usuario = userId)).fold(
                onSuccess = { direction ->
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            createSuccess = true,
                            directions = it.directions + direction
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isCreating = false, error = error.message) }
                }
            )
        }
    }

    fun updateDirection(id: Int, request: DirectionRequest) {
        _uiState.update { it.copy(isCreating = true, error = null) }
        viewModelScope.launch {
            directionUseCase.update(id, request).fold(
                onSuccess = { updated ->
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            createSuccess = true,
                            directions = it.directions.map { d -> if (d.id == updated.id) updated else d }
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isCreating = false, error = error.message) }
                }
            )
        }
    }

    fun deleteDirection(id: Int) {
        viewModelScope.launch {
            directionUseCase.delete(id).fold(
                onSuccess = {
                    _uiState.update { it.copy(directions = it.directions.filter { d -> d.id != id }) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearCreateSuccess() {
        _uiState.update { it.copy(createSuccess = false) }
    }
}