package com.miltonvaz.voltio1.features.products.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio1.core.network.TokenManager

import com.miltonvaz.voltio1.features.company.domain.usecase.GetCompanyByUserIdUseCase
import com.miltonvaz.voltio1.features.company.domain.usecase.GetCompanyProductsUseCase
import com.miltonvaz.voltio1.features.products.domain.usecase.DeleteProductUseCase
import com.miltonvaz.voltio1.features.products.domain.usecase.GetProductsUseCase
import com.miltonvaz.voltio1.features.products.presentation.screens.UiState.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCompanyProductsUseCase: GetCompanyProductsUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getCompanyByUserIdUseCase: GetCompanyByUserIdUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private suspend fun resolveCompanyId(): Int? {
        val cached = tokenManager.getCompanyId()
        if (cached != -1) return cached

        val token = tokenManager.getToken() ?: return null
        val userId = tokenManager.getUserId()
        if (userId == -1) return null

        return getCompanyByUserIdUseCase(token, userId).getOrNull()?.let { company ->
            tokenManager.saveCompanyId(company.id)
            company.id
        }
    }

    fun loadProducts() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val role = tokenManager.getUserRole()

            val result = if (role == "company" || role == "admin") {
                val companyId = resolveCompanyId()
                if (companyId == null) {
                    _uiState.update { it.copy(isLoading = false, error = "No se pudo obtener la empresa") }
                    return@launch
                }
                getCompanyProductsUseCase(companyId)
            } else {
                getProductsUseCase(token)
            }

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