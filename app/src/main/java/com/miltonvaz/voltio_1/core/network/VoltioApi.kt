package com.miltonvaz.voltio_1.core.network

import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface VoltioApi {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body authRequest: AuthRequest): AuthResponse

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
}
