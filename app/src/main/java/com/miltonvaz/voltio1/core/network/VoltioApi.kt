package com.miltonvaz.voltio1.core.network

import com.miltonvaz.voltio1.features.auth.data.datasource.remote.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface VoltioApi {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body authRequest: AuthRequest): AuthResponse

    @POST("auth/register/company")
    suspend fun registerCompany(@Body companyRequest: CompanyRegisterRequest): AuthResponse

    @Multipart
    @POST("auth/register/company")
    suspend fun registerCompanyWithLogo(
        @Part("name") name: RequestBody,
        @Part("lastname") lastname: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("nombre_comercial") commercialName: RequestBody,
        @Part("direccion_empresa") address: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("telefono_contacto") contactPhone: RequestBody,
        @Part("correo_contacto") contactEmail: RequestBody,
        @Part("role") role: RequestBody,
        @Part("account_type") accountType: RequestBody,
        @Part logo: MultipartBody.Part? = null
    ): AuthResponse

    @POST("auth/register-delivery")
    suspend fun registerDelivery(@Body deliveryRequest: DeliveryRegisterRequest): AuthResponse

    @POST("auth/google")
    suspend fun googleAuth(@Body googleRequest: GoogleAuthRequest): AuthResponse

    @POST("auth/google-progresive")
    suspend fun googleAuthProgresive(@Body googleRequest: GoogleAuthRequest): AuthResponse

    @POST("auth/logout")
    suspend fun logout(): MessageResponse

    @POST("auth/refresh")
    suspend fun refreshToken(): MessageResponse

    @GET("auth/profile")
    suspend fun getProfile(): ProfileResponse

    @GET("auth/verify")
    suspend fun verifyToken(): ProfileResponse

    @POST("auth/fcm-token")
    suspend fun registerFCMToken(
        @Header("Authorization") token: String,
        @Body request: RegisterFCMTokenRequest
    ): MessageResponse

    @GET("empresas/{id}")
    suspend fun getCompanyById(@Path("id") id: Int): CompanyDto

    @GET("products/empresa/{id}")
    suspend fun getCompanyProducts(@Path("id") id: Int): List<com.miltonvaz.voltio1.features.products.data.datasource.remote.model.ProductDto>

    @GET("usuarios/{id}/empresa")
    suspend fun getCompanyByUserId(
        @Path("id") userId: Int
    ): CompanyDto
}
