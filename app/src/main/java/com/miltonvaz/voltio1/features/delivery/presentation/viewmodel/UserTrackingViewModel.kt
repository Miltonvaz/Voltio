package com.miltonvaz.voltio1.features.delivery.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.miltonvaz.voltio1.features.delivery.domain.usecase.JoinOrderTrackingUseCase
import com.miltonvaz.voltio1.features.delivery.domain.usecase.LeaveOrderTrackingUseCase
import com.miltonvaz.voltio1.features.delivery.domain.usecase.ObserveDriverLocationUseCase
import com.miltonvaz.voltio1.features.delivery.presentation.screens.UiState.UserTrackingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserTrackingViewModel @Inject constructor(
    private val observeDriverLocationUseCase: ObserveDriverLocationUseCase,
    private val joinOrderTrackingUseCase: JoinOrderTrackingUseCase,
    private val leaveOrderTrackingUseCase: LeaveOrderTrackingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserTrackingUiState())
    val uiState = _uiState.asStateFlow()

    fun startTracking(orderId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            joinOrderTrackingUseCase(orderId)
            
            observeDriverLocationUseCase()
                .onEach { location ->
                    _uiState.update { 
                        it.copy(
                            driverLocation = LatLng(location.first, location.second),
                            isLoading = false 
                        ) 
                    }
                }
                .catch { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect()
        }
    }

    fun stopTracking(orderId: Int) {
        viewModelScope.launch {
            leaveOrderTrackingUseCase(orderId)
        }
    }
}
