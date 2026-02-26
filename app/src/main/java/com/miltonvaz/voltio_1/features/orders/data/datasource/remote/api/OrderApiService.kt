package com.miltonvaz.voltio_1.features.orders.data.datasource.remote.api

import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.MessageResponse
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.model.OrderDto
import retrofit2.http.*

interface OrderApiService {
    @GET("ordenes")
    suspend fun getAllOrders(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String
    ): List<OrderDto>

    @GET("ordenes/{id}")
    suspend fun getOrderById(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id") id: Int
    ): OrderDto

    @GET("usuarios/{id_usuario}/ordenes")
    suspend fun getOrdersByUserId(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id_usuario") userId: Int
    ): List<OrderDto>

    @POST("ordenes")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Body order: OrderDto
    ): OrderDto

    // 1. Actualiza la Base de Datos (API Principal)
    @PUT("ordenes/{id}")
    suspend fun updateOrderInDatabase(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id") id: Int,
        @Body order: OrderDto
    ): OrderDto

    // 2. Dispara la Notificación (Bridge WebSocket/MQTT)
    @PUT("https://voltio-ws.ameth.shop/ordenes/actualizar")
    suspend fun notifyOrderUpdate(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Body order: OrderDto
    ): MessageResponse

    @DELETE("ordenes/{id}")
    suspend fun deleteOrder(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id") id: Int
    )
}
