package com.miltonvaz.voltio_1.features.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.core.network.TokenManager
import com.miltonvaz.voltio_1.features.orders.domain.usecase.GetOrdersUseCase
import com.miltonvaz.voltio_1.features.orders.domain.usecase.ObserveNewOrdersUseCase
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.OrdersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminOrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val observeNewOrdersUseCase: ObserveNewOrdersUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAllOrders()
        observeLiveOrders()
    }

    fun loadAllOrders() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val result = getOrdersUseCase(token)
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
                _uiState.update { currentState ->
                    val currentList = currentState.orders.toMutableList()
                    val index = currentList.indexOfFirst { it.id == newOrder.id }
                    if (index != -1) currentList[index] = newOrder
                    else currentList.add(0, newOrder)
                    currentState.copy(orders = currentList)
                }
            }
        }
    }
}
