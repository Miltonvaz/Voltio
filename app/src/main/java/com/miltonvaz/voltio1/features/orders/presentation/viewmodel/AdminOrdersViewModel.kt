package com.miltonvaz.voltio1.features.orders.presentation.viewmodel

import android.util.Log
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
            Log.d("ORDERS_VM", "resolveCompanyId: usando caché id=$cached")
            companyId = cached
            return cached
        }

        // No abortamos si el token es null — el CookieJar maneja la sesión
        val token = tokenManager.getToken() ?: ""
        val userId = tokenManager.getUserId()
        if (userId == -1) {
            Log.e("ORDERS_VM", "resolveCompanyId: userId no guardado, no se puede resolver empresa")
            return null
        }

        Log.d("ORDERS_VM", "resolveCompanyId: consultando empresa para userId=$userId")
        return getCompanyByUserIdUseCase(token, userId).fold(
            onSuccess = { company ->
                Log.d("ORDERS_VM", "resolveCompanyId: empresa encontrada id=${company.id}")
                tokenManager.saveCompanyId(company.id)
                companyId = company.id
                company.id
            },
            onFailure = { error ->
                Log.e("ORDERS_VM", "resolveCompanyId: fallo al obtener empresa - ${error.message}", error)
                null
            }
        )
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
