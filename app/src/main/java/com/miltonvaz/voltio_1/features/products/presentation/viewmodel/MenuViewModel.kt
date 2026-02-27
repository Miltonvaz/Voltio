package com.miltonvaz.voltio_1.features.products.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.core.network.ISocketManager
import com.miltonvaz.voltio_1.core.network.TokenManager
import com.miltonvaz.voltio_1.features.orders.domain.usecase.GetOrdersUseCase
import com.miltonvaz.voltio_1.features.orders.domain.usecase.ObserveNewOrdersUseCase
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
    private val getOrdersUseCase: GetOrdersUseCase,
    private val observeNewOrdersUseCase: ObserveNewOrdersUseCase,
    private val socketManager: ISocketManager,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDashboardData()
        socketManager.connect()
        observeLiveUpdates()
    }

    fun loadDashboardData() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            
            val productsResult = getProductsUseCase(token)
            val ordersResult = getOrdersUseCase(token)

            _uiState.update { currentState ->
                val products = productsResult.getOrNull() ?: emptyList()
                val orders = ordersResult.getOrNull() ?: emptyList()

                val totalStockValue = products.sumOf { product ->
                    val stockCount = if (product.stock <= 0) 0 else product.stock
                    product.price * stockCount
                }

                val lowStockProducts = products.filter { it.stock <= 5 }.take(5)

                currentState.copy(
                    isLoading = false,
                    products = lowStockProducts,
                    totalOrdersToday = orders.size,
                    totalStockValue = totalStockValue,
                    error = if (productsResult.isFailure) productsResult.exceptionOrNull()?.message else null
                )
            }
        }
    }

    private fun observeLiveUpdates() {
        viewModelScope.launch {
            observeNewOrdersUseCase().collect { _ ->
                // Cuando llega una nueva orden, refrescamos los datos del dashboard
                // Esto actualizará el contador de pedidos y el stock si hubo ventas
                loadDashboardData()
            }
        }
    }
}
