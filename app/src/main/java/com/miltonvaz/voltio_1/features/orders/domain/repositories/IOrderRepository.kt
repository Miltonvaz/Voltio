package com.miltonvaz.voltio_1.features.orders.domain.repositories

import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import kotlinx.coroutines.flow.Flow

interface IOrderRepository {
    suspend fun getAllOrders(token: String): List<Order>
    suspend fun getOrderById(token: String, id: Int): Order
    suspend fun getOrdersByUserId(token: String, userId: Int): List<Order>
    suspend fun createOrder(token: String, order: Order): Order
    suspend fun updateOrder(token: String, id: Int, order: Order)
    suspend fun deleteOrder(token: String, id: Int)

    fun observeNewOrders(): Flow<Order>
}
