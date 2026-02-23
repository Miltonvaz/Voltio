package com.miltonvaz.voltio_1.features.products.domain.usecase

import com.miltonvaz.voltio_1.features.products.data.datasource.remote.model.CreateProductRequest
import com.miltonvaz.voltio_1.features.products.domain.entities.Product
import com.miltonvaz.voltio_1.features.products.domain.repositories.IProductRepository
import jakarta.inject.Inject


class CreateProductUseCase @Inject constructor(
    private val repository: IProductRepository
) {
    suspend operator fun invoke(
        token: String,
        request: CreateProductRequest,
        imageBytes: ByteArray?
    ): Result<Product> {
        return try {
            val product = repository.createProduct(token, request, imageBytes)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}