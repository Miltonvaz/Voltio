package com.miltonvaz.voltio_1.features.orders.domain.usecase

import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(
    private val repository: IOrderRepository
) {
    suspend operator fun invoke(token: String, id: Int, order: Order, last4: String = ""): Result<Unit> {
        return try {
            repository.updateOrder(token, id, order, last4)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
