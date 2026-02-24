package com.miltonvaz.voltio_1.features.orders.domain.repositories

import com.miltonvaz.voltio_1.features.orders.domain.entities.Order

interface IOrderRepository {
    suspend fun getAllOrders(): List<Order>
    suspend fun getOrderById(id: Int): Order
    suspend fun getOrdersByUserId(userId: Int): List<Order>
    suspend fun createOrder(order: Order): Order
    suspend fun updateOrder(id: Int, order: Order): Order
    suspend fun deleteOrder(id: Int)
}
