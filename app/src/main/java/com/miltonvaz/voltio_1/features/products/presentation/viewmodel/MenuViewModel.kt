package com.miltonvaz.voltio_1.features.products.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.core.network.TokenManager
import com.miltonvaz.voltio_1.features.orders.domain.usecase.GetOrdersUseCase
import com.miltonvaz.voltio_1.features.products.domain.usecase.GetProductsUseCase
import com.miltonvaz.voltio_1.features.products.presentation.screens.UiState.MenuUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getOrdersUseCase: GetOrdersUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDashboardData()
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

                // Cálculo monetario del stock real
                val totalStockValue = products.sumOf { product ->
                    val stockCount = if (product.stock <= 0) 0 else product.stock
                    product.price * stockCount
                }

                // Lógica de conteo de pedidos más flexible
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                
                // Contamos pedidos:
                // 1. Que coincidan con la fecha de hoy
                // 2. O si solo hay uno en la base de datos y hoy no hubo ninguno, 
                //    podemos mostrar el total histórico o el más reciente para depuración
                val todayOrdersCount = orders.count { it.orderDate.contains(today) }
                
                // Si hoy es 0 pero sabemos que hay datos en la API, 
                // mostramos el total para confirmar que la conexión funciona
                val finalOrdersDisplay = if (todayOrdersCount == 0 && orders.isNotEmpty()) {
                    orders.size // Mostramos el total histórico si no hay de hoy
                } else {
                    todayOrdersCount
                }

                currentState.copy(
                    isLoading = false,
                    products = products.filter { it.stock < 10 }.take(5),
                    totalOrdersToday = finalOrdersDisplay,
                    totalStockValue = totalStockValue,
                    error = if (productsResult.isFailure) productsResult.exceptionOrNull()?.message else null
                )
            }
        }
    }
}
