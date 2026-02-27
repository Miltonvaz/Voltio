package com.miltonvaz.voltio_1.features.products.data.datasource.remote

import com.miltonvaz.voltio_1.features.products.data.datasource.remote.model.ProductDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String
    ): List<ProductDto>

    @GET("products/{id}")
    suspend fun getProductById(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id") id: Int
    ): ProductDto

    @Multipart
    @POST("products")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Part("sku") sku: RequestBody,
        @Part("nombre") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody?,
        @Part("precio_venta") precio_venta: RequestBody,
        @Part("stock_actual") stock_actual: RequestBody,
        @Part("id_categoria") id_categoria: RequestBody?,
        @Part imagen: MultipartBody.Part? = null
    ): ProductDto

    @Multipart
    @PUT("products/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id") id: Int,
        @Part("sku") sku: RequestBody,
        @Part("nombre") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody?,
        @Part("precio_venta") precio_venta: RequestBody,
        @Part("stock_actual") stock_actual: RequestBody,
        @Part("id_categoria") id_categoria: RequestBody?,
        @Part imagen: MultipartBody.Part? = null
    ): ProductDto

    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id") id: Int
    ): Response<Unit>

    @FormUrlEncoded
    @PATCH("products/{id}")
    suspend fun updateStock(
        @Header("Authorization") token: String,
        @Header("Cookie") cookie: String,
        @Path("id") id: Int,
        @Field("stock_actual") stock: Int
    ): ProductDto
}
