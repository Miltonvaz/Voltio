package com.miltonvaz.voltio_1.features.products.domain.usecase

import com.miltonvaz.voltio_1.features.products.domain.repositories.IProductRepository
import jakarta.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: IProductRepository
) {
    suspend operator fun invoke(token: String, id: Int): Result<Unit> {
        return try {
            repository.deleteProduct(token, id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}