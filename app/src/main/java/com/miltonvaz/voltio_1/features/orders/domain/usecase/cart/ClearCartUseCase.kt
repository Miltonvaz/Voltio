package com.miltonvaz.voltio_1.features.orders.domain.usecase.cart

import com.miltonvaz.voltio_1.features.orders.domain.repositories.ICartRepository
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(
    private val repository: ICartRepository
) {
    suspend operator fun invoke() = repository.clearCart()
}
