package com.miltonvaz.voltio_1.features.orders.domain.usecase

import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import javax.inject.Inject

class DeleteOrderUseCase @Inject constructor(
    private val repository: IOrderRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return try {
            Result.success(repository.deleteOrder(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
