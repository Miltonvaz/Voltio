package com.miltonvaz.voltio1.features.orders.domain.usecase.cart

import com.miltonvaz.voltio1.features.orders.domain.repositories.ICartRepository
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(
    private val repository: ICartRepository
) {
    suspend operator fun invoke(productId: Int) = repository.removeItem(productId)
}
