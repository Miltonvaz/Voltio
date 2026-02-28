package com.miltonvaz.voltio_1.features.orders.domain.usecase

import android.util.Log
import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.entities.OrderStatus
import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import com.miltonvaz.voltio_1.features.products.domain.repositories.IProductRepository
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(
    private val repository: IOrderRepository,
    private val productRepository: IProductRepository
) {
    suspend operator fun invoke(token: String, id: Int, order: Order, last4: String = ""): Result<Unit> {
        return try {
            val currentOrderFromServer = repository.getOrderById(token, id)
            Log.d("UpdateOrder", "Status actual: ${currentOrderFromServer.status}, Nuevo status: ${order.status}")
            
            repository.updateOrder(token, id, order, last4)
            
            if (order.status == OrderStatus.COMPLETED && currentOrderFromServer.status != OrderStatus.COMPLETED) {
                Log.d("UpdateOrder", "Descontando stock para ${currentOrderFromServer.products.size} productos")
                currentOrderFromServer.products.forEach { item ->
                    try {
                        val product = productRepository.getProductById(token, item.productId)
                        val newStock = product.stock - item.quantity
                        Log.d("UpdateOrder", "Producto: ${product.name}, Stock anterior: ${product.stock}, Vendido: ${item.quantity}, Nuevo stock: $newStock")
                        productRepository.updateStock(token, item.productId, if (newStock < 0) 0 else newStock)
                    } catch (e: Exception) {
                        Log.e("UpdateOrder", "Error actualizando stock para producto ${item.productId}", e)
                    }
                }
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("UpdateOrder", "Error general en UpdateOrderStatusUseCase", e)
            Result.failure(e)
        }
    }
}
