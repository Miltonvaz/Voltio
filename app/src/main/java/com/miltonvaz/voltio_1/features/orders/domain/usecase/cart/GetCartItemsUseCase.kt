package com.miltonvaz.voltio_1.features.orders.domain.usecase.cart

import com.miltonvaz.voltio_1.features.orders.domain.entities.CartItem
import com.miltonvaz.voltio_1.features.orders.domain.repositories.ICartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val repository: ICartRepository
) {
    operator fun invoke(): Flow<List<CartItem>> = repository.getCartItems()
}
