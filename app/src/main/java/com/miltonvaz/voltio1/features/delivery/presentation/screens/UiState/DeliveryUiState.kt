package com.miltonvaz.voltio1.features.delivery.presentation.screens.UiState

import com.google.android.gms.maps.model.LatLng
import com.miltonvaz.voltio1.features.orders.domain.entities.Order

data class DeliveryUiState(
    val assignedOrders: List<Order> = emptyList(),
    val currentTrackingOrderId: Int? = null,
    val currentLocation: LatLng? = null,
    val routePoints: List<LatLng> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val completingOrder: Boolean = false,
    val orderCompleted: Boolean = false,
    val locationPermissionGranted: Boolean = false
)