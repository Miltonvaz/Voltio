package com.miltonvaz.voltio_1.features.products.domain.usecase

import com.miltonvaz.voltio_1.features.products.data.datasource.remote.model.CreateProductRequest
import com.miltonvaz.voltio_1.features.products.domain.entities.Product
import com.miltonvaz.voltio_1.features.products.domain.repositories.IProductRepository

class UpdateProductUseCase(private val repository: IProductRepository) {
    suspend operator fun invoke(
        token: String,
        id: Int,
        request: CreateProductRequest,
        imageBytes: ByteArray? = null
    ): Result<Product> {
        return try {
            val product = repository.updateProduct(token, id, request, imageBytes)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}