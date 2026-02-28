package com.miltonvaz.voltio_1.features.products.domain.usecase

import com.miltonvaz.voltio_1.features.products.domain.entities.Product
import com.miltonvaz.voltio_1.features.products.domain.repositories.IProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: IProductRepository
) {
    suspend operator fun invoke(token: String): Result<List<Product>> {
        return try {
            val products = repository.getProducts(token)
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}