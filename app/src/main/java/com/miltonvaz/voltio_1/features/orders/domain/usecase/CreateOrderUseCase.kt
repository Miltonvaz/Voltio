package com.miltonvaz.voltio_1.features.orders.domain.usecase

import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import com.miltonvaz.voltio_1.features.products.domain.repositories.IProductRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val repository: IOrderRepository,
    private val productRepository: IProductRepository
) {
    suspend operator fun invoke(token: String, order: Order, last4: String): Result<Order> {
        return try {
            val createdOrder = repository.createOrder(token, order, last4)
            
            order.products.forEach { item ->
                try {
                    val product = productRepository.getProductById(token, item.productId)
                    val newStock = product.stock - item.quantity
                    productRepository.updateStock(token, item.productId, if (newStock < 0) 0 else newStock)
                } catch (e: Exception) {
                    // Silently fail stock update if one product fails, or handle accordingly
                }
            }
            
            Result.success(createdOrder)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
