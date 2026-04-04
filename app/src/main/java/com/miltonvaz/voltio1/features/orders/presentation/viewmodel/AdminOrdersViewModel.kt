package com.miltonvaz.voltio1.features.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio1.core.hardware.domain.VibrationManager
import com.miltonvaz.voltio1.core.network.TokenManager
import com.miltonvaz.voltio1.features.company.domain.usecase.GetCompanyByUserIdUseCase
import com.miltonvaz.voltio1.features.orders.domain.usecase.GetOrdersByCompanyIdUseCase
import com.miltonvaz.voltio1.features.orders.domain.usecase.ObserveNewOrdersUseCase
import com.miltonvaz.voltio1.features.orders.presentation.screens.UiState.OrdersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminOrdersViewModel @Inject constructor(
    private val getOrdersByCompanyIdUseCase: GetOrdersByCompanyIdUseCase,
    private val getCompanyByUserIdUseCase: GetCompanyByUserIdUseCase,
    private val observeNewOrdersUseCase: ObserveNewOrdersUseCase,
    private val tokenManager: TokenManager,
    private val vibrationManager: VibrationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState = _uiState.asStateFlow()

    private var companyId: Int? = null

    init {
        loadAllOrders()
        observeLiveOrders()
    }

    private suspend fun resolveCompanyId(): Int? {
        companyId?.let { return it }

        val cached = tokenManager.getCompanyId()
        if (cached != -1) {
            companyId = cached
            return cached
        }

        val token = tokenManager.getToken() ?: return null
        val userId = tokenManager.getUserId()
        if (userId == -1) return null

        return getCompanyByUserIdUseCase(token, userId).getOrNull()?.let { company ->
            tokenManager.saveCompanyId(company.id)
            companyId = company.id
            company.id
        }
    }

    fun loadAllOrders() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val resolvedCompanyId = resolveCompanyId()

            if (resolvedCompanyId == null) {
                _uiState.update { it.copy(isLoading = false, error = "No se pudo obtener la empresa") }
                return@launch
            }

            val result = getOrdersByCompanyIdUseCase(token, resolvedCompanyId)
            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { orders ->
                        currentState.copy(isLoading = false, orders = orders, error = null)
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, error = error.message)
                    }
                )
            }
        }
    }

    private fun observeLiveOrders() {
        viewModelScope.launch {
            observeNewOrdersUseCase().collect { newOrder ->
                // Solo procesar órdenes de esta empresa
                val myCompanyId = resolveCompanyId()
                if (myCompanyId != null && newOrder.companyId != null && newOrder.companyId != myCompanyId) {
                    return@collect
                }

                var isNewOrder = false
                
                _uiState.update { currentState ->
                    val currentList = currentState.orders.toMutableList()
                    val index = currentList.indexOfFirst { it.id == newOrder.id }
                    
                    if (index != -1) {
                        currentList[index] = newOrder
                    } else {
                        isNewOrder = true
                        currentList.add(0, newOrder)
                    }
                    currentState.copy(orders = currentList)
                }

                if (isNewOrder) {
                    vibrationManager.vibrate(500)
                }
            }
        }
    }
}
