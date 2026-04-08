package com.miltonvaz.voltio1.features.orders.data.datasource.remote.api

import com.miltonvaz.voltio1.features.orders.data.datasource.remote.model.CapturePayPalOrderRequest
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.model.CapturePayPalOrderResponse
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.model.CreatePayPalOrderRequest
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.model.CreatePayPalOrderResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PayPalApiService {

    @POST("paypal/create-order")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Body request: CreatePayPalOrderRequest
    ): CreatePayPalOrderResponse

    @POST("paypal/capture-order")
    suspend fun captureOrder(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Body request: CapturePayPalOrderRequest
    ): CapturePayPalOrderResponse
}
