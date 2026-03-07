package com.miltonvaz.voltio_1.features.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.features.orders.domain.entities.CartItem
import com.miltonvaz.voltio_1.features.orders.domain.usecase.cart.*
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.CartUiState
import com.miltonvaz.voltio_1.features.products.domain.entities.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            getCartItemsUseCase().collect { items ->
                _uiState.update { it.copy(cartItems = items) }
            }
        }
    }

    fun addItem(product: Product, quantity: Int) {
        viewModelScope.launch {
            addToCartUseCase(CartItem(product, quantity))
        }
    }

    fun removeItem(productId: Int) {
        viewModelScope.launch {
            removeFromCartUseCase(productId)
        }
    }

    fun increaseQuantity(productId: Int) {
        viewModelScope.launch {
            val currentItem = _uiState.value.cartItems.find { it.product.id == productId }
            if (currentItem != null) {
                updateCartQuantityUseCase(productId, currentItem.quantity + 1)
            }
        }
    }

    fun decreaseQuantity(productId: Int) {
        viewModelScope.launch {
            val currentItem = _uiState.value.cartItems.find { it.product.id == productId }
            if (currentItem != null && currentItem.quantity > 1) {
                updateCartQuantityUseCase(productId, currentItem.quantity - 1)
            }
        }
    }

    fun clearCart() {
        // Implement clear if needed via a UseCase
    }
}
