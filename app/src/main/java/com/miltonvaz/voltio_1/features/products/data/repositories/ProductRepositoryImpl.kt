package com.miltonvaz.voltio_1.features.products.data.repositories

import com.miltonvaz.voltio_1.features.products.data.datasource.remote.ProductApiService
import com.miltonvaz.voltio_1.features.products.data.datasource.remote.mapper.toDomain
import com.miltonvaz.voltio_1.features.products.data.datasource.remote.model.CreateProductRequest
import com.miltonvaz.voltio_1.features.products.domain.entities.Product
import com.miltonvaz.voltio_1.features.products.domain.repositories.IProductRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProductRepositoryImpl(
    private val api: ProductApiService
) : IProductRepository {

    private fun formatAuth(token: String) = "Bearer $token"
    private fun formatCookie(token: String) = "access_token=$token"

    override suspend fun getProducts(token: String): List<Product> {
        val response = api.getProducts(formatAuth(token), formatCookie(token))
        return response.map { it.toDomain() }
    }

    override suspend fun getProductById(token: String, id: Int): Product {
        val response = api.getProductById(formatAuth(token), formatCookie(token), id)
        return response.toDomain()
    }

    override suspend fun createProduct(
        token: String,
        request: CreateProductRequest,
        imageBytes: ByteArray?
    ): Product {
        // Usamos el operador elvis (?: "") para asegurar que nunca sea nulo al convertir
        val skuPart = request.sku.toRequestBody("text/plain".toMediaTypeOrNull())
        val nombrePart = request.nombre.toRequestBody("text/plain".toMediaTypeOrNull())

        // Solución al error: Si es nulo, enviamos un string vacío o un valor por defecto
        val descPart = (request.descripcion ?: "").toRequestBody("text/plain".toMediaTypeOrNull())

        val precioPart = request.precio_venta.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = request.stock_actual.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        // Solución al error para id_categoria
        val catPart = (request.id_categoria?.toString() ?: "0").toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = imageBytes?.let {
            val requestFile = it.toRequestBody("image/jpeg".toMediaTypeOrNull())
            // "image" es el campo que tu backend de Express busca en req.file
            MultipartBody.Part.createFormData("imagen", "product_${request.sku}.jpg", requestFile)
        }

        val response = api.createProduct(
            token = formatAuth(token),
            cookie = formatCookie(token),
            sku = skuPart,
            nombre = nombrePart,
            descripcion = descPart,
            precio_venta = precioPart,
            stock_actual = stockPart,
            id_categoria = catPart,
            imagen = imagePart
        )

        return response.toDomain()
    }

    override suspend fun updateProduct(
        token: String,
        id: Int,
        request: CreateProductRequest,
        imageBytes: ByteArray?
    ): Product {
        val skuPart = request.sku.toRequestBody("text/plain".toMediaTypeOrNull())
        val nombrePart = request.nombre.toRequestBody("text/plain".toMediaTypeOrNull())
        val descPart = (request.descripcion ?: "").toRequestBody("text/plain".toMediaTypeOrNull())
        val precioPart = request.precio_venta.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = request.stock_actual.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val catPart = (request.id_categoria?.toString() ?: "1").toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = imageBytes?.let {
            val requestFile = it.toRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imagen", "update_${id}.jpg", requestFile)
        }

        val response = api.updateProduct(
            token = formatAuth(token),
            cookie = formatCookie(token),
            id = id,
            sku = skuPart,
            nombre = nombrePart,
            descripcion = descPart,
            precio_venta = precioPart,
            stock_actual = stockPart,
            id_categoria = catPart,
            imagen = imagePart
        )

        return response.toDomain()
    }
    override suspend fun deleteProduct(token: String, id: Int) {
        api.deleteProduct(formatAuth(token), formatCookie(token), id)
    }
}