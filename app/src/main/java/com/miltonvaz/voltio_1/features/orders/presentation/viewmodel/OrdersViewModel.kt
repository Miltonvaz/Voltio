package com.miltonvaz.voltio_1.features.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.features.orders.domain.usecase.GetOrdersUseCase
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.OrdersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = getOrdersUseCase()
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
}
