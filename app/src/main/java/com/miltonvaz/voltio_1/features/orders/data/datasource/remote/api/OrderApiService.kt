package com.miltonvaz.voltio_1.features.orders.data.datasource.remote.api

import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.model.OrderDto
import retrofit2.http.*

interface OrderApiService {
    @GET("ordenes")
    suspend fun getAllOrders(): List<OrderDto>

    @GET("ordenes/{id}")
    suspend fun getOrderById(@Path("id") id: Int): OrderDto

    @GET("usuarios/{id_usuario}/ordenes")
    suspend fun getOrdersByUserId(@Path("id_usuario") userId: Int): List<OrderDto>

    @POST("ordenes")
    suspend fun createOrder(@Body order: OrderDto): OrderDto

    @PUT("ordenes/{id}")
    suspend fun updateOrder(@Path("id") id: Int, @Body order: OrderDto): OrderDto

    @DELETE("ordenes/{id}")
    suspend fun deleteOrder(@Path("id") id: Int)
}
