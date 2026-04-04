package com.miltonvaz.voltio1.features.orders.domain.repositories

import com.miltonvaz.voltio1.features.orders.domain.entities.Order
import kotlinx.coroutines.flow.Flow

interface IOrderRepository {
    suspend fun getAllOrders(token: String): List<Order>
    suspend fun getOrderById(token: String, id: Int): Order
    suspend fun getOrdersByUserId(token: String, userId: Int): List<Order>
    suspend fun getOrdersByCompanyId(token: String, companyId: Int): List<Order>
    suspend fun createOrder(token: String, order: Order, last4: String): Order
    suspend fun updateOrder(token: String, id: Int, order: Order, last4: String)
    suspend fun deleteOrder(token: String, id: Int)
    
    // Delivery methods
    suspend fun getAssignedOrders(token: String, repartidorId: Int): List<Order>
    suspend fun updateOrderStatus(token: String, orderId: Int, status: String, userId: Int): Result<Unit>
    suspend fun completeOrderDelivery(token: String, orderId: Int, userId: Int): Result<Unit>
    suspend fun assignOrder(token: String, orderId: Int, repartidorId: Int): Result<Unit>

    fun observeNewOrders(): Flow<Order>
}
