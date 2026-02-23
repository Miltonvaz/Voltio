package com.miltonvaz.voltio_1.features.products.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.core.network.TokenManager
import com.miltonvaz.voltio_1.features.products.data.datasource.remote.model.CreateProductRequest
import com.miltonvaz.voltio_1.features.products.domain.usecase.CreateProductUseCase
import com.miltonvaz.voltio_1.features.products.domain.usecase.GetProductByIdUseCase
import com.miltonvaz.voltio_1.features.products.domain.usecase.UpdateProductUseCase
import com.miltonvaz.voltio_1.features.products.presentation.screens.UiState.ProductFormUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductFormViewModel(
    private val productId: Int,
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductFormUiState())
    val uiState = _uiState.asStateFlow()

    var nombre by mutableStateOf("")
    var precio by mutableStateOf("")
    var stock by mutableStateOf("")
    var sku by mutableStateOf("")
    var descripcion by mutableStateOf("")
    var categoriaId by mutableStateOf(1)
    var imageName by mutableStateOf("Subir imagen del producto")
    var selectedImageBytes by mutableStateOf<ByteArray?>(null)

    init {
        if (productId != -1) {
            loadProductForEdit(productId)
        }
    }

    private fun loadProductForEdit(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val token = tokenManager.getToken() ?: ""
            val result = getProductByIdUseCase(token, id)
            result.onSuccess { product ->
                _uiState.update { it.copy(currentProduct = product, isLoading = false) }
                nombre = product.name
                precio = product.price.toString()
                stock = product.stock.toString()
                sku = product.sku
                descripcion = product.description
            }
            result.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    fun saveProduct(id: Int, onNavigateBack: () -> Unit) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val request = CreateProductRequest(
                sku = sku,
                nombre = nombre,
                descripcion = descripcion,
                precio_venta = precio.toDoubleOrNull() ?: 0.0,
                stock_actual = stock.toIntOrNull() ?: 0,
                imagen_url = _uiState.value.currentProduct?.imageUrl,
                id_categoria = categoriaId
            )

            val result = if (id == -1) {
                createProductUseCase(token, request, selectedImageBytes)
            } else {
                updateProductUseCase(token, id, request, selectedImageBytes)
            }

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                onNavigateBack()
            }
            result.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    fun clearState() {
        _uiState.value = ProductFormUiState()
    }
}