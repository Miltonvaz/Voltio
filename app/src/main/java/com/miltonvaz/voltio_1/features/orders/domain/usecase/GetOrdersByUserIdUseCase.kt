package com.miltonvaz.voltio_1.features.orders.domain.usecase

import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import javax.inject.Inject

class GetOrdersByUserIdUseCase @Inject constructor(
    private val repository: IOrderRepository
) {
    suspend operator fun invoke(token: String, userId: Int): Result<List<Order>> {
        return try {
            Result.success(repository.getOrdersByUserId(token, userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
