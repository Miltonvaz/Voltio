package com.miltonvaz.voltio1.features.delivery.presentation.screens.UiState

import com.google.android.gms.maps.model.LatLng

data class UserTrackingUiState(
    val driverLocation: LatLng? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
