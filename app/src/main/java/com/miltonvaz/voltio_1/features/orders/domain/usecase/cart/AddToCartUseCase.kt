package com.miltonvaz.voltio_1.features.orders.domain.usecase.cart

import com.miltonvaz.voltio_1.features.orders.domain.entities.CartItem
import com.miltonvaz.voltio_1.features.orders.domain.repositories.ICartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val repository: ICartRepository
) {
    suspend operator fun invoke(item: CartItem) = repository.addItem(item)
}
