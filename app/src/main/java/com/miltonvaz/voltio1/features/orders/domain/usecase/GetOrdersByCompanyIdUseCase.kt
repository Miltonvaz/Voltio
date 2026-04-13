package com.miltonvaz.voltio1.features.orders.domain.usecase

import android.util.Log
import com.miltonvaz.voltio1.features.orders.domain.entities.Order
import com.miltonvaz.voltio1.features.orders.domain.repositories.IOrderRepository
import javax.inject.Inject

class GetOrdersByCompanyIdUseCase @Inject constructor(
    private val repository: IOrderRepository
) {
    suspend operator fun invoke(token: String, companyId: Int): Result<List<Order>> {
        return try {
            Log.d("ORDERS_UC", "GET empresas/$companyId/ordenes — token=${if (token.isBlank()) "VACÍO" else "OK"}")
            val orders = repository.getOrdersByCompanyId(token, companyId)
            Log.d("ORDERS_UC", "Respuesta OK: ${orders.size} pedidos")
            Result.success(orders)
        } catch (e: Exception) {
            Log.e("ORDERS_UC", "Error: ${e.message}", e)
            Result.failure(e)
        }
    }
}
