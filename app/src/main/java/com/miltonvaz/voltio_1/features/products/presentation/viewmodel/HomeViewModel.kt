package com.miltonvaz.voltio_1.features.products.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.core.network.TokenManager
import com.miltonvaz.voltio_1.features.products.domain.usecase.DeleteProductUseCase
import com.miltonvaz.voltio_1.features.products.domain.usecase.GetProductsUseCase
import com.miltonvaz.voltio_1.features.products.presentation.screens.UiState.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val result = getProductsUseCase(token)

            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { products ->
                        currentState.copy(isLoading = false, products = products, error = null)
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, error = error.message)
                    }
                )
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val result = deleteProductUseCase(token, id)

            result.onSuccess {
                loadProducts()
            }.onFailure { error ->
                _uiState.update { it.copy(error = "No se pudo eliminar: ${error.message}") }
            }
        }
    }
}