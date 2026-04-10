package com.miltonvaz.voltio1.features.orders.domain.usecase

import com.miltonvaz.voltio1.features.orders.domain.entities.Order
import com.miltonvaz.voltio1.features.orders.domain.repositories.IOrderRepository
import javax.inject.Inject

class GetOrdersByCompanyIdUseCase @Inject constructor(
    private val repository: IOrderRepository
) {
    suspend operator fun invoke(token: String, companyId: Int): Result<List<Order>> {
        return try {
            Result.success(repository.getOrdersByCompanyId(token, companyId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
