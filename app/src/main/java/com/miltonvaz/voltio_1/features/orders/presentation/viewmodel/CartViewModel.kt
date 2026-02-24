package com.miltonvaz.voltio_1.features.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.features.orders.data.manager.CartManager
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.CartUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartManager: CartManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            cartManager.items.collect { items ->
                _uiState.update { it.copy(cartItems = items) }
            }
        }
    }

    fun removeItem(productId: Int) {
        cartManager.removeItem(productId)
    }

    fun increaseQuantity(productId: Int) {
        cartManager.updateQuantity(productId, 1)
    }

    fun decreaseQuantity(productId: Int) {
        cartManager.updateQuantity(productId, -1)
    }

    fun clearCart() {
        cartManager.clear()
    }
}
