package com.miltonvaz.voltio_1.features.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.core.network.TokenManager
import com.miltonvaz.voltio_1.features.orders.data.manager.CartManager
import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.entities.OrderItem
import com.miltonvaz.voltio_1.features.orders.domain.usecase.CreateOrderUseCase
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.AddressInfo
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.CardInfo
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.CheckoutUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartManager: CartManager,
    private val createOrderUseCase: CreateOrderUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState = _uiState.asStateFlow()

    fun updateCardInfo(
        number: String = _uiState.value.cardInfo.number,
        name: String = _uiState.value.cardInfo.name,
        expiry: String = _uiState.value.cardInfo.expiry,
        cvv: String = _uiState.value.cardInfo.cvv
    ) {
        _uiState.update { it.copy(cardInfo = CardInfo(number, name, expiry, cvv)) }
    }

    fun updateAddressInfo(
        street: String = _uiState.value.addressInfo.street,
        city: String = _uiState.value.addressInfo.city,
        state: String = _uiState.value.addressInfo.state,
        zipCode: String = _uiState.value.addressInfo.zipCode,
        reference: String? = _uiState.value.addressInfo.reference
    ) {
        _uiState.update { it.copy(addressInfo = AddressInfo(street, city, state, zipCode, reference)) }
    }

    fun placeOrder() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            val cartItems = cartManager.items.value
            if (cartItems.isEmpty()) {
                _uiState.update { it.copy(isLoading = false, error = "El carrito está vacío") }
                return@launch
            }

            val totalAmount = cartItems.sumOf { it.product.price * it.quantity }
            val orderDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            
            val order = Order(
                id = 0,
                userId = 0,
                orderDate = orderDate,
                status = "Pendiente",
                totalAmount = totalAmount,
                description = "Pedido a la calle ${_uiState.value.addressInfo.street}",
                products = cartItems.map { 
                    OrderItem(
                        productId = it.product.id,
                        productName = it.product.name,
                        quantity = it.quantity,
                        unitPrice = it.product.price
                    )
                }
            )

            val result = createOrderUseCase(order)
            
            _uiState.update { currentState ->
                result.fold(
                    onSuccess = {
                        cartManager.clear()
                        currentState.copy(isLoading = false, orderPlacedSuccessfully = true)
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, orderPlacedSuccessfully = false, error = error.message)
                    }
                )
            }
        }
    }
}
