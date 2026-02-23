package com.miltonvaz.voltio_1.features.products.domain.repositories

import com.miltonvaz.voltio_1.features.products.data.datasource.remote.model.CreateProductRequest
import com.miltonvaz.voltio_1.features.products.domain.entities.Product

interface IProductRepository {
    suspend fun getProducts(token: String): List<Product>
    suspend fun getProductById(token: String, id: Int): Product

    suspend fun createProduct(
        token: String,
        request: CreateProductRequest,
        imageBytes: ByteArray?
    ): Product

    suspend fun updateProduct(
        token: String,
        id: Int,
        request: CreateProductRequest,
        imageBytes: ByteArray?
    ): Product
    suspend fun deleteProduct(token: String, id: Int)
}