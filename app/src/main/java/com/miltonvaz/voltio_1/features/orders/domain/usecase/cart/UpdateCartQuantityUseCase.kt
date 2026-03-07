package com.miltonvaz.voltio_1.features.orders.domain.usecase.cart

import com.miltonvaz.voltio_1.features.orders.domain.repositories.ICartRepository
import javax.inject.Inject

class UpdateCartQuantityUseCase @Inject constructor(
    private val repository: ICartRepository
) {
    suspend operator fun invoke(productId: Int, quantity: Int) = repository.updateQuantity(productId, quantity)
}
