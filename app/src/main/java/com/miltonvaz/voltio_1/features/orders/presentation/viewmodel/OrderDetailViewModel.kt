package com.miltonvaz.voltio_1.features.orders.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.core.network.TokenManager
import com.miltonvaz.voltio_1.features.orders.domain.usecase.GetOrderByIdUseCase
import com.miltonvaz.voltio_1.features.orders.domain.usecase.ObserveNewOrdersUseCase
import com.miltonvaz.voltio_1.features.orders.domain.usecase.UpdateOrderStatusUseCase
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.OrderDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase,
    private val observeNewOrdersUseCase: ObserveNewOrdersUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState = _uiState.asStateFlow()

    val orderId: Int = savedStateHandle.get<Int>("orderId") ?: -1

    init {
        if (orderId != -1) {
            loadOrderDetail(orderId)
            observeLiveUpdates()
        }
    }

    private fun observeLiveUpdates() {
        viewModelScope.launch {
            observeNewOrdersUseCase().collect { updatedOrder ->
                if (updatedOrder.id == orderId) {
                    _uiState.update { it.copy(order = updatedOrder) }
                }
            }
        }
    }

    fun loadOrderDetail(id: Int) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val result = getOrderByIdUseCase(token, id)
            
            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { order ->
                        currentState.copy(isLoading = false, order = order, error = null)
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, error = error.message)
                    }
                )
            }
        }
    }

    fun updateStatus(newStatus: String) {
        val currentOrder = _uiState.value.order ?: return
        _uiState.update { it.copy(isUpdating = true) }
        
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val updatedOrder = currentOrder.copy(status = newStatus)
            val result = updateOrderStatusUseCase(token, currentOrder.id, updatedOrder)
            
            _uiState.update { currentState ->
                result.fold(
                    onSuccess = {
                        // Como el UseCase ahora devuelve Result<Unit>, no intentamos asignar el resultado a 'order'
                        // Confiamos en el WebSocket para la actualización reactiva o actualizamos con el objeto local 'updatedOrder'
                        currentState.copy(isUpdating = false, order = updatedOrder, error = null)
                    },
                    onFailure = { error ->
                        currentState.copy(isUpdating = false, error = error.message)
                    }
                )
            }
        }
    }
}
