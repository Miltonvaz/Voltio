package com.miltonvaz.voltio1.features.orders.data.repositories

import com.miltonvaz.voltio1.features.orders.data.datasource.remote.api.PayPalApiService
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.model.CapturePayPalOrderRequest
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.model.CapturePayPalOrderResponse
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.model.CreatePayPalOrderRequest
import com.miltonvaz.voltio1.features.orders.data.datasource.remote.model.CreatePayPalOrderResponse
import javax.inject.Inject

class PayPalRepository @Inject constructor(
    private val api: PayPalApiService
) {
    private fun formatAuth(token: String) = "Bearer $token"
    private fun formatCookie(token: String) = "access_token=$token"

    suspend fun createOrder(token: String, monto: Double): Result<CreatePayPalOrderResponse> {
        return try {
            val response = api.createOrder(
                formatAuth(token),
                formatCookie(token),
                CreatePayPalOrderRequest(monto = monto)
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun captureOrder(token: String, orderID: String): Result<CapturePayPalOrderResponse> {
        return try {
            val response = api.captureOrder(
                formatAuth(token),
                formatCookie(token),
                CapturePayPalOrderRequest(orderID = orderID)
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
