package com.miltonvaz.voltio1.features.orders.data.datasource.remote.api

import com.miltonvaz.voltio1.features.auth.data.datasource.remote.model.MessageResponse
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.model.*
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

    @GET("empresas/{id_empresa}/ordenes")
    suspend fun getOrdersByCompanyId(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id_empresa") companyId: Int
    ): List<OrderDto>

    @POST("ordenes")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Body order: OrderDto
    ): OrderDto

    @PUT("ordenes/{id}")
    suspend fun updateOrderInDatabase(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id") id: Int,
        @Body order: OrderUpdateRequestDto
    ): MessageResponse

    @PUT("https://voltio-ws.ameth.shop/ordenes/actualizar")
    suspend fun notifyOrderUpdate(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Body update: OrderStatusUpdateDto
    ): MessageResponse

    @PUT("ordenes/{id}")
    suspend fun updateOrderStatusPut(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id") id: Int,
        @Body statusRequest: OrderStatusUpdateDto
    ): MessageResponse

    @DELETE("ordenes/{id}")
    suspend fun deleteOrder(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id") id: Int
    )

    // Delivery Endpoints
    @GET("repartidor/{id_repartidor}/mi-ruta")
    suspend fun getAssignedOrders(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id_repartidor") repartidorId: Int
    ): List<OrderDto>

    @PATCH("ordenes/{id}/estado")
    suspend fun updateOrderStatus(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id") id: Int,
        @Body statusRequest: OrderStatusUpdateDto
    ): MessageResponse

    @PATCH("ordenes/{id}/asignar")
    suspend fun assignOrder(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id") orderId: Int,
        @Body body: Map<String, Int>
    ): MessageResponse
}
