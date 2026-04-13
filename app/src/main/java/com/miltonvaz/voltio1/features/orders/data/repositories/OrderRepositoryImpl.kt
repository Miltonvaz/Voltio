package com.miltonvaz.voltio1.features.orders.data.repositories

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.miltonvaz.voltio1.core.network.ISocketManager
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.api.OrderApiService
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.mapper.toDomain
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.mapper.toDto
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.mapper.toUpdateRequest
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.model.OrderDto
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.model.OrderStatusUpdateDto
import com.miltonvaz.voltio1.features.orders.domain.entities.Order
import com.miltonvaz.voltio1.features.orders.domain.repositories.IOrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: OrderApiService,
    private val socketManager: ISocketManager,
    private val gson: Gson
) : IOrderRepository {

    // Si el token está vacío, pasa null para que Retrofit omita el header
    // y el CookieJar maneje la autenticación por sesión
    private fun formatAuth(token: String): String? = if (token.isNotBlank()) "Bearer $token" else null
    private fun formatCookie(token: String): String? = if (token.isNotBlank()) "access_token=$token" else null

    override suspend fun getAllOrders(token: String): List<Order> {
        return api.getAllOrders(formatAuth(token), formatCookie(token)).map { it.toDomain() }
    }

    override suspend fun getOrderById(token: String, id: Int): Order {
        return api.getOrderById(formatAuth(token), formatCookie(token), id).toDomain()
    }

    override suspend fun getOrdersByUserId(token: String, userId: Int): List<Order> {
        return api.getOrdersByUserId(formatAuth(token), formatCookie(token), userId).map { it.toDomain() }
    }

    override suspend fun getOrdersByCompanyId(token: String, companyId: Int): List<Order> {
        return api.getOrdersByCompanyId(formatAuth(token), formatCookie(token), companyId).map { it.toDomain() }
    }

    override suspend fun createOrder(token: String, order: Order, last4: String): Order {
        val orderDto = order.toDto(last4)
        val response = api.createOrder(formatAuth(token), formatCookie(token), orderDto)
        return response.toDomain()
    }

    override suspend fun updateOrder(token: String, id: Int, order: Order, last4: String) {
        val requestDto = order.toUpdateRequest(last4)
        api.updateOrderInDatabase(formatAuth(token), formatCookie(token), id, requestDto)

        val updatePayload = OrderStatusUpdateDto(id, order.userId, order.status.apiValue)
        socketManager.emit("orden_actualizada", gson.toJson(updatePayload))
        api.notifyOrderUpdate(formatAuth(token), formatCookie(token), updatePayload)
    }

    override suspend fun deleteOrder(token: String, id: Int) {
        api.deleteOrder(formatAuth(token), formatCookie(token), id)
    }

    override suspend fun getAssignedOrders(token: String, repartidorId: Int): List<Order> {
        return try {
            api.getAssignedOrders(formatAuth(token), formatCookie(token), repartidorId).map { it.toDomain() }
        } catch (e: Exception) {
            Log.e("ORDER_REPO", "Error fetching assigned orders for repartidor $repartidorId: ${e.message}")
            emptyList()
        }
    }

    override suspend fun updateOrderStatus(token: String, orderId: Int, status: String, userId: Int): Result<Unit> {
        return try {
            val request = OrderStatusUpdateDto(orderId, userId, status)
            api.updateOrderStatus(formatAuth(token), formatCookie(token), orderId, request)
            socketManager.emit("orden_actualizada", gson.toJson(request))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun completeOrderDelivery(token: String, orderId: Int, userId: Int): Result<Unit> {
        return try {
            val request = OrderStatusUpdateDto(orderId, userId, "completada")
            api.updateOrderStatusPut(formatAuth(token), formatCookie(token), orderId, request)
            socketManager.emit("orden_actualizada", gson.toJson(request))
            try {
                api.notifyOrderUpdate(formatAuth(token), formatCookie(token), request)
            } catch (e: Exception) {
                Log.w("ORDER_REPO", "WS notification failed (non-fatal): ${e.message}")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun assignOrder(token: String, orderId: Int, repartidorId: Int): Result<Unit> {
        return try {
            val body = mapOf("id_repartidor" to repartidorId)
            api.assignOrder(formatAuth(token), formatCookie(token), orderId, body)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeNewOrders(): Flow<Order> {
        socketManager.connect()
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
