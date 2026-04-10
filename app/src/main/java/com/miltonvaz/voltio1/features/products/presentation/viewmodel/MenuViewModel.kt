package com.miltonvaz.voltio1.features.products.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio1.core.network.ISocketManager
import com.miltonvaz.voltio1.core.network.TokenManager
import com.miltonvaz.voltio1.core.notifications.domain.usecase.UnsubscribeFromTopicUseCase
import com.miltonvaz.voltio1.features.company.domain.usecase.GetCompanyByUserIdUseCase
import com.miltonvaz.voltio1.features.company.domain.usecase.GetCompanyProductsUseCase
import com.miltonvaz.voltio1.features.orders.domain.usecase.GetOrdersByCompanyIdUseCase
import com.miltonvaz.voltio1.features.orders.domain.usecase.ObserveNewOrdersUseCase
import com.miltonvaz.voltio1.features.products.presentation.screens.UiState.MenuUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getCompanyProductsUseCase: GetCompanyProductsUseCase,
    private val getOrdersByCompanyIdUseCase: GetOrdersByCompanyIdUseCase,
    private val getCompanyByUserIdUseCase: GetCompanyByUserIdUseCase,
    private val observeNewOrdersUseCase: ObserveNewOrdersUseCase,
    private val unsubscribeFromTopicUseCase: UnsubscribeFromTopicUseCase,
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

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // Dejar de recibir notificaciones de admin al salir
            unsubscribeFromTopicUseCase("admin_orders")
            tokenManager.clearSession()
            onSuccess()
        }
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

    fun loadDashboardData() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val companyId = resolveCompanyId()

            if (companyId == null) {
                _uiState.update { it.copy(isLoading = false, error = "No se pudo obtener la empresa") }
                return@launch
            }

            val productsResult = getCompanyProductsUseCase(companyId)
            val ordersResult = getOrdersByCompanyIdUseCase(token, companyId)

            _uiState.update { currentState ->
                val products = productsResult.getOrNull() ?: emptyList()
                val orders = ordersResult.getOrNull() ?: emptyList()

                val totalStockValue = products.sumOf { it.price * it.stock }
                val lowStockProducts = products.filter { it.stock <= 5 }.take(5)

                currentState.copy(
                    isLoading = false,
                    products = lowStockProducts,
                    totalProducts = products.size,
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
                loadDashboardData()
            }
        }
    }
}
