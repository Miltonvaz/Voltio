package com.miltonvaz.voltio_1.features.orders.data.repositories

import com.miltonvaz.voltio_1.core.database.dao.CartDao
import com.miltonvaz.voltio_1.features.orders.data.datasource.local.mapper.toDomain
import com.miltonvaz.voltio_1.features.orders.data.datasource.local.mapper.toEntity
import com.miltonvaz.voltio_1.features.orders.domain.entities.CartItem
import com.miltonvaz.voltio_1.features.orders.domain.repositories.ICartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : ICartRepository {

    override fun getCartItems(): Flow<List<CartItem>> = cartDao.getAllCartItems().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun addItem(item: CartItem) {
        val currentItems = getCartItems().first()
        val existing = currentItems.find { it.product.id == item.product.id }
        if (existing != null) {
            val newQty = existing.quantity + item.quantity
            cartDao.updateQuantity(item.product.id, newQty)
        } else {
            cartDao.insertItem(item.toEntity())
        }
    }

    override suspend fun removeItem(productId: Int) {
        cartDao.deleteById(productId)
    }

    override suspend fun updateQuantity(productId: Int, quantity: Int) {
        cartDao.updateQuantity(productId, quantity)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}
