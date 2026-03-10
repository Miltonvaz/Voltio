package com.miltonvaz.voltio_1.features.orders.domain.repositories

import com.miltonvaz.voltio_1.features.orders.domain.entities.CartItem
import kotlinx.coroutines.flow.Flow

interface ICartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addItem(item: CartItem)
    suspend fun removeItem(productId: Int)
    suspend fun updateQuantity(productId: Int, quantity: Int)
    suspend fun clearCart()
}
