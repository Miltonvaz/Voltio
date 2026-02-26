package com.miltonvaz.voltio_1.features.orders.data.manager

import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartManager @Inject constructor() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items = _items.asStateFlow()

    fun addItem(item: CartItem) {
        _items.update { current ->
            val existing = current.find { it.product.id == item.product.id }
            if (existing != null) {
                current.map {
                    if (it.product.id == item.product.id) it.copy(quantity = it.quantity + item.quantity)
                    else it
                }
            } else {
                current + item
            }
        }
    }

    fun removeItem(productId: Int) {
        _items.update { it.filter { item -> item.product.id != productId } }
    }

    fun updateQuantity(productId: Int, delta: Int) {
        _items.update { current ->
            current.map {
                if (it.product.id == productId) {
                    val newQty = (it.quantity + delta).coerceAtLeast(1)
                    it.copy(quantity = newQty)
                } else it
            }
        }
    }

    fun clear() {
        _items.update { emptyList() }
    }
}
