package com.miltonvaz.voltio1.features.orders.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio1.core.network.TokenManager
import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.model.RepartidorDto
import com.miltonvaz.voltio1.features.delivery.domain.repositories.IDeliveryRepository
import com.miltonvaz.voltio1.features.orders.domain.entities.OrderStatus
import com.miltonvaz.voltio1.features.orders.domain.repositories.IOrderRepository
import com.miltonvaz.voltio1.features.orders.domain.usecase.GetOrderByIdUseCase
import com.miltonvaz.voltio1.features.orders.domain.usecase.ObserveNewOrdersUseCase
import com.miltonvaz.voltio1.features.orders.domain.usecase.UpdateOrderStatusUseCase
import com.miltonvaz.voltio1.features.orders.presentation.screens.UiState.OrderDetailUiState
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
    private val orderRepository: IOrderRepository,
    private val deliveryRepository: IDeliveryRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _availableDrivers = MutableStateFlow<List<RepartidorDto>>(emptyList())
    val availableDrivers = _availableDrivers.asStateFlow()

    val orderId: Int = savedStateHandle.get<Int>("orderId") ?: -1

    init {
        if (orderId != -1) {
            loadOrderDetail(orderId)
            observeLiveUpdates()
            loadAvailableDrivers()
        }
    }

    private fun observeLiveUpdates() {
        viewModelScope.launch {
            observeNewOrdersUseCase().collect { updatedOrder ->
                if (updatedOrder.id == orderId) {
                    loadOrderDetail(orderId)
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

    fun loadAvailableDrivers() {
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            deliveryRepository.getAvailableDrivers(token).onSuccess { drivers ->
                _availableDrivers.value = drivers
            }.onFailure { error ->
                Log.e("ORDER_DETAIL", "Error al cargar repartidores: ${error.message}")
            }
        }
    }

    fun assignOrderToDriver(idRepartidor: Int) {
        _uiState.update { it.copy(isAssigning = true) }
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            orderRepository.assignOrder(token, orderId, idRepartidor).fold(
                onSuccess = {
                    // Actualización local inmediata para tiempo real
                    val driver = _availableDrivers.value.find { it.idRepartidor == idRepartidor }
                    _uiState.update { currentState ->
                        currentState.copy(
                            isAssigning = false,
                            order = currentState.order?.copy(
                                deliveryPersonId = idRepartidor,
                                deliveryPersonName = driver?.let { "${it.name} ${it.lastname}" }
                            )
                        )
                    }
                    // Opcional: Recargar desde el servidor para sincronizar
                    // loadOrderDetail(orderId)
                },
                onFailure = { error ->
                    Log.e("ORDER_DETAIL", "Error al asignar repartidor: ${error.message}")
                    _uiState.update { it.copy(isAssigning = false, error = "Fallo al asignar: ${error.message}") }
                }
            )
        }
    }

    fun updateStatus(newStatus: OrderStatus) {
        val currentOrder = _uiState.value.order ?: return
        _uiState.update { it.copy(isUpdating = true) }
        
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val updatedOrder = currentOrder.copy(status = newStatus)
            val result = updateOrderStatusUseCase(token, currentOrder.id, updatedOrder, currentOrder.last4 ?: "")
            
            result.onSuccess {
                _uiState.update { currentState ->
                    currentState.copy(isUpdating = false, order = updatedOrder, error = null)
                }
                loadOrderDetail(orderId)
            }.onFailure { error ->
                _uiState.update { currentState ->
                    currentState.copy(isUpdating = false, error = error.message)
                }
            }
        }
    }
}
