package com.miltonvaz.voltio_1.features.orders.domain.usecase

import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import javax.inject.Inject

class GetOrderByIdUseCase @Inject constructor(
    private val repository: IOrderRepository
) {
    suspend operator fun invoke(id: Int): Result<Order> {
        return try {
            Result.success(repository.getOrderById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
