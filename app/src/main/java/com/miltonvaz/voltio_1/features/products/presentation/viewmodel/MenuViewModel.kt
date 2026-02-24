package com.miltonvaz.voltio_1.features.products.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.core.network.TokenManager
import com.miltonvaz.voltio_1.features.products.domain.usecase.GetProductsUseCase
import com.miltonvaz.voltio_1.features.products.presentation.screens.UiState.MenuUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadRecentActivity()
    }

    fun loadRecentActivity() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val result = getProductsUseCase(token)
            
            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { products ->
                        currentState.copy(isLoading = false, products = products.take(5), error = null)
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, error = error.message)
                    }
                )
            }
        }
    }
}
