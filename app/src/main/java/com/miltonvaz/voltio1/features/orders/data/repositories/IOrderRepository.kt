package com.miltonvaz.voltio1.features.orders.data.repositories

import com.miltonvaz.voltio1.features.orders.domain.entities.Order // Asegúrate de tener esta entidad
import kotlinx.coroutines.flow.Flow

interface IOrderRepository {
    fun getOrdersByUserId(userId: Int): Flow<List<Order>>
    suspend fun createOrder(order: Order): Result<Unit>
}