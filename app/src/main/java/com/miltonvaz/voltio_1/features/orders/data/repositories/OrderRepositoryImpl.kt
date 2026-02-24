package com.miltonvaz.voltio_1.features.orders.data.repositories

import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.api.OrderApiService
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.mapper.toDomain
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.mapper.toDto
import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import jakarta.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: OrderApiService
) : IOrderRepository {
    override suspend fun getAllOrders(): List<Order> {
        return api.getAllOrders().map { it.toDomain() }
    }

    override suspend fun getOrderById(id: Int): Order {
        return api.getOrderById(id).toDomain()
    }

    override suspend fun getOrdersByUserId(userId: Int): List<Order> {
        return api.getOrdersByUserId(userId).map { it.toDomain() }
    }

    override suspend fun createOrder(order: Order): Order {
        return api.createOrder(order.toDto()).toDomain()
    }

    override suspend fun updateOrder(id: Int, order: Order): Order {
        return api.updateOrder(id, order.toDto()).toDomain()
    }

    override suspend fun deleteOrder(id: Int) {
        api.deleteOrder(id)
    }
}
