package com.miltonvaz.voltio_1.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.AuthRequest
import com.miltonvaz.voltio_1.features.auth.domain.usecase.AuthUseCase
import com.miltonvaz.voltio_1.features.auth.presentation.screens.register.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun register(name: String, lastName: String, email: String, pass: String, phone: String?) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val request = AuthRequest(
                name = name,
                lastname = lastName,
                email = email,
                password = pass,
                phone = phone
            )

            val result = authUseCase.register(request)

            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { response ->
                        currentState.copy(
                            isLoading = false,
                            user = response.user,
                            isSuccess = true,
                            error = null
                        )
                    },
                    onFailure = { error ->
                        currentState.copy(
                            isLoading = false,
                            error = error.message ?: "Error desconocido",
                            isSuccess = false
                        )
                    }
                )
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}