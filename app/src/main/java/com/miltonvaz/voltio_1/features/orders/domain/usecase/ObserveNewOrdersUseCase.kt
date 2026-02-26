    package com.miltonvaz.voltio_1.features.orders.domain.usecase

import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNewOrdersUseCase @Inject constructor(
    private val repository: IOrderRepository
) {
    operator fun invoke(): Flow<Order> {
        return repository.observeNewOrders()
    }
}
