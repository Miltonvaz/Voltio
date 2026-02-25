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

    private fun formatAuth(token: String) = "Bearer $token"
    private fun formatCookie(token: String) = "access_token=$token"

    override suspend fun getAllOrders(token: String): List<Order> {
        return api.getAllOrders(formatAuth(token), formatCookie(token)).map { it.toDomain() }
    }

    override suspend fun getOrderById(token: String, id: Int): Order {
        return api.getOrderById(formatAuth(token), formatCookie(token), id).toDomain()
    }

    override suspend fun getOrdersByUserId(token: String, userId: Int): List<Order> {
        return api.getOrdersByUserId(formatAuth(token), formatCookie(token), userId).map { it.toDomain() }
    }

    override suspend fun createOrder(token: String, order: Order): Order {
        return api.createOrder(formatAuth(token), formatCookie(token), order.toDto()).toDomain()
    }

    override suspend fun updateOrder(token: String, id: Int, order: Order): Order {
        return api.updateOrder(formatAuth(token), formatCookie(token), id, order.toDto()).toDomain()
    }

    override suspend fun deleteOrder(token: String, id: Int) {
        api.deleteOrder(formatAuth(token), formatCookie(token), id)
    }
}
