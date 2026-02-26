package com.miltonvaz.voltio_1.features.products.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.core.network.TokenManager
import com.miltonvaz.voltio_1.features.products.domain.entities.Product
import com.miltonvaz.voltio_1.features.products.domain.usecase.DeleteProductUseCase
import com.miltonvaz.voltio_1.features.products.domain.usecase.GetProductsUseCase
import com.miltonvaz.voltio_1.features.products.presentation.screens.UiState.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMockProducts() // ← temporal para preview
    }

    private fun loadMockProducts() {
        _uiState.update {
            it.copy(
                isLoading = false,
                products = listOf(
                    Product(1, "SKU001", "Protoboard", "Protoboard 830 puntos", 49.35, 10, null, 1, "2024-01-01"),
                    Product(2, "SKU002", "Estaño 60/40", "Estaño para soldar", 49.35, 10, null, 1, "2024-01-01"),
                    Product(3, "SKU003", "ESP32", "Microcontrolador WiFi", 18.35, 10, null, 1, "2024-01-01"),
                    Product(4, "SKU004", "Cautín", "Cautín de punta fina", 89.35, 10, null, 1, "2024-01-01"),
                    Product(5, "SKU005", "Raspberry Pi", "Raspberry Pi 4", 229.77, 10, null, 1, "2024-01-01"),
                    Product(6, "SKU006", "Capacitor", "Capacitor electrolítico", 49.35, 10, null, 1, "2024-01-01"),
                    Product(7, "SKU007", "Resistencias", "Pack 100 resistencias", 19.35, 10, null, 1, "2024-01-01"),
                    Product(8, "SKU008", "Leds", "Pack leds de colores", 15.35, 10, null, 1, "2024-01-01")
                )
            )
        }
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