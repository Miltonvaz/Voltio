package com.miltonvaz.voltio_1.features.orders.domain.usecase

import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(
    private val repository: IOrderRepository
) {
    suspend operator fun invoke(token: String, id: Int, order: Order): Result<Order> {
        return try {
            Result.success(repository.updateOrder(token, id, order))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
