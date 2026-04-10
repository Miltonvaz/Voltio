package com.miltonvaz.voltio1.features.delivery.domain.usecase

import com.miltonvaz.voltio1.features.orders.domain.entities.Order
import com.miltonvaz.voltio1.features.orders.domain.repositories.IOrderRepository
import javax.inject.Inject

class GetAssignedOrdersUseCase @Inject constructor(
    private val repository: IOrderRepository
) {
    suspend operator fun invoke(token: String, repartidorId: Int): Result<List<Order>> {
        return try {
            Result.success(repository.getAssignedOrders(token, repartidorId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
