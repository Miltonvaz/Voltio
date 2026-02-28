package com.miltonvaz.voltio_1.features.orders.domain.usecase

import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: IOrderRepository
) {
    suspend operator fun invoke(token: String): Result<List<Order>> {
        return try {
            Result.success(repository.getAllOrders(token))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
