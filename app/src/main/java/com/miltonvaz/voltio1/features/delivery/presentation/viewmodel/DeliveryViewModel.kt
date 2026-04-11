package com.miltonvaz.voltio1.features.delivery.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.miltonvaz.voltio1.core.network.TokenManager
import com.miltonvaz.voltio1.features.auth.domain.repositories.IAuthRepository
import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.api.OpenRouteService
import com.miltonvaz.voltio1.features.delivery.domain.usecase.*
import com.miltonvaz.voltio1.features.delivery.presentation.screens.UiState.DeliveryUiState
import com.miltonvaz.voltio1.features.orders.domain.repositories.IOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    private val getAssignedOrdersUseCase: GetAssignedOrdersUseCase,
    private val observeLocationUpdatesUseCase: ObserveLocationUpdatesUseCase,
    private val sendLocationUseCase: SendLocationUseCase,
    private val joinOrderTrackingUseCase: JoinOrderTrackingUseCase,
    private val leaveOrderTrackingUseCase: LeaveOrderTrackingUseCase,
    private val openRouteService: OpenRouteService,
    private val authRepository: IAuthRepository,
    private val tokenManager: TokenManager,
    private val orderRepository: IOrderRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeliveryUiState())
    val uiState = _uiState.asStateFlow()

    private var locationJob: Job? = null
    private val ORS_API_KEY = "eyJvcmciOiI1YjNjZTM1OTc4NTExMTAwMDFjZjYyNDgiLCJpZCI6IjkwNTVkOWY2ZGQ0OTQ4YTE5OTBiZTNmYTlmZjdjMjQyIiwiaCI6Im11cm11cjY0In0="

    fun onLocationPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(locationPermissionGranted = granted) }
        if (granted) {
            loadAssignedOrders()
        }
    }

    fun loadAssignedOrders() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token.isNullOrEmpty()) {
                    _uiState.update { it.copy(isLoading = false, error = "Sesión no válida") }
                    return@launch
                }

                val profile = authRepository.getProfile()
                val repartidorId = profile.user.id

                getAssignedOrdersUseCase(token, repartidorId).fold(
                    onSuccess = { orders ->
                        _uiState.update { it.copy(isLoading = false, assignedOrders = orders, error = null) }
                    },
                    onFailure = {
                        _uiState.update { it.copy(isLoading = false, error = "Error al cargar entregas") }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error de conexión") }
            }
        }
    }

    fun startDelivery(orderId: Int) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, currentTrackingOrderId = orderId) }
                joinOrderTrackingUseCase(orderId)
                startLocationReporting(orderId)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                Log.e("DELIVERY_VM", "Error al iniciar entrega: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = "No se pudo iniciar el seguimiento") }
            }
        }
    }

    private fun startLocationReporting(orderId: Int) {
        locationJob?.cancel()
        locationJob = viewModelScope.launch {
            try {
                observeLocationUpdatesUseCase()
                    .onEach { location ->
                        val latLng = LatLng(location.latitude, location.longitude)
                        _uiState.update { it.copy(currentLocation = latLng) }
                        sendLocationUseCase(orderId, location.latitude, location.longitude)

                        val order = _uiState.value.assignedOrders.find { it.id == _uiState.value.currentTrackingOrderId }
                        if (order?.latitude != null && order.longitude != null && _uiState.value.routePoints.isEmpty()) {
                            fetchRouteORS(latLng, LatLng(order.latitude!!, order.longitude!!))
                        }
                    }
                    .catch { e ->
                        Log.e("DELIVERY_GPS", "Error de GPS: ${e.message}")
                    }
                    .collect()
            } catch (e: Exception) {
                Log.e("DELIVERY_GPS", "Fallo en hilo de GPS: ${e.message}")
            }
        }
    }

    private fun fetchRouteORS(origin: LatLng, destination: LatLng) {
        viewModelScope.launch {
            try {
                val start = "${origin.longitude},${origin.latitude}"
                val end = "${destination.longitude},${destination.latitude}"

                val response = openRouteService.getDirections(ORS_API_KEY, start, end)

                val coords = response.features?.get(0)?.geometry?.coordinates
                if (!coords.isNullOrEmpty()) {
                    val points = coords.map { LatLng(it[1], it[0]) }
                    _uiState.update { it.copy(routePoints = points) }
                }
            } catch (e: Exception) {
                Log.e("ORS_API", "Error fetching route: ${e.message}")
            }
        }
    }

    fun stopDelivery() {
        val orderId = _uiState.value.currentTrackingOrderId ?: return
        viewModelScope.launch {
            try {
                locationJob?.cancel()
                leaveOrderTrackingUseCase(orderId)
                _uiState.update { it.copy(currentTrackingOrderId = null, currentLocation = null, routePoints = emptyList()) }
            } catch (e: Exception) {
                Log.e("DELIVERY_VM", "Error al detener entrega: ${e.message}")
            }
        }
    }

    fun completeOrder(orderId: Int) {
        _uiState.update { it.copy(completingOrder = true, error = null) }
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken() ?: ""
                val profile = authRepository.getProfile()
                val userId = profile.user.id

                orderRepository.completeOrderDelivery(token, orderId, userId).fold(
                    onSuccess = {
                        locationJob?.cancel()
                        leaveOrderTrackingUseCase(orderId)
                        _uiState.update {
                            it.copy(
                                completingOrder = false,
                                orderCompleted = true,
                                currentTrackingOrderId = null,
                                currentLocation = null,
                                routePoints = emptyList(),
                                assignedOrders = it.assignedOrders.filter { o -> o.id != orderId }
                            )
                        }
                    },
                    onFailure = { error ->
                        Log.e("DELIVERY_VM", "Error al completar pedido: ${error.message}")
                        _uiState.update {
                            it.copy(completingOrder = false, error = "No se pudo completar el pedido")
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e("DELIVERY_VM", "Error de conexión: ${e.message}")
                _uiState.update { it.copy(completingOrder = false, error = "Error de conexión") }
            }
        }
    }

    fun resetOrderCompleted() {
        _uiState.update { it.copy(orderCompleted = false) }
    }

    override fun onCleared() {
        super.onCleared()
        locationJob?.cancel()
    }
}