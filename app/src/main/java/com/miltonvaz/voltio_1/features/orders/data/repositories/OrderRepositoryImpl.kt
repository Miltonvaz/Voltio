package com.miltonvaz.voltio_1.features.orders.data.repositories

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.miltonvaz.voltio_1.core.network.ISocketManager
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.api.OrderApiService
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.mapper.toDomain
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.mapper.toDto
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.model.OrderDto
import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class OrderRepositoryImpl @Inject constructor(
    private val api: OrderApiService,
    private val socketManager: ISocketManager, // Inyectado directamente, estilo Retrofit
    private val gson: Gson
) : IOrderRepository {

    private val TAG = "OrderRepositoryImpl"

    private fun formatAuth(token: String) = "Bearer $token"
    private fun formatCookie(token: String) = "access_token=$token"

    override suspend fun getAllOrders(token: String): List<Order> {
        return try {
            val response = api.getAllOrders(formatAuth(token), formatCookie(token))
            response.map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getOrderById(token: String, id: Int): Order {
        return api.getOrderById(formatAuth(token), formatCookie(token), id).toDomain()
    }

    override suspend fun getOrdersByUserId(token: String, userId: Int): List<Order> {
        return try {
            api.getOrdersByUserId(formatAuth(token), formatCookie(token), userId).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun createOrder(token: String, order: Order, last4: String): Order {
        return api.createOrder(formatAuth(token), formatCookie(token), order.toDto(last4)).toDomain()
    }

    override suspend fun updateOrder(token: String, id: Int, order: Order, last4: String) {
        try {
            val dbPayload = OrderDto(status = order.status)
            api.updateOrderInDatabase(formatAuth(token), formatCookie(token), id, dbPayload)
            api.notifyOrderUpdate(formatAuth(token), formatCookie(token), order.toDto(last4))
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteOrder(token: String, id: Int) {
        api.deleteOrder(formatAuth(token), formatCookie(token), id)
    }

    override fun observeNewOrders(): Flow<Order> {
        socketManager.connect() // Asegura conexión
        return socketManager.observeOrders().mapNotNull { json ->
            try {
                val jsonObject = gson.fromJson(json, JsonObject::class.java)
                val orderData = if (jsonObject.has("datos")) jsonObject.get("datos") else jsonObject
                gson.fromJson(orderData, OrderDto::class.java).toDomain()
            } catch (e: Exception) {
                null
            }
        }
    }
}
