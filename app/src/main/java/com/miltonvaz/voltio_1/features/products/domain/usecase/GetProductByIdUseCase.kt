package com.miltonvaz.voltio_1.features.products.domain.usecase

import com.miltonvaz.voltio_1.features.products.domain.entities.Product
import com.miltonvaz.voltio_1.features.products.domain.repositories.IProductRepository
import jakarta.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val repository: IProductRepository
) {
    suspend operator fun invoke(token: String, id: Int): Result<Product> {
        return try {
            val product = repository.getProductById(token, id)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}