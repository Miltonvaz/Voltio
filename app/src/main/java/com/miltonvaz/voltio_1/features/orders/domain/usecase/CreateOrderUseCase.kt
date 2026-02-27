package com.miltonvaz.voltio_1.features.orders.domain.usecase

import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val repository: IOrderRepository
) {
    suspend operator fun invoke(token: String, order: Order, last4: String): Result<Order> {
        return try {
            Result.success(repository.createOrder(token, order, last4))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
