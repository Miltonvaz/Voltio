package com.miltonvaz.voltio1.features.auth.domain.repositories

import com.miltonvaz.voltio1.features.auth.data.datasource.remote.model.*
import com.miltonvaz.voltio1.features.auth.domain.entities.Company

interface IAuthRepository {
    suspend fun login(request: LoginRequest): AuthResponse
    suspend fun register(request: AuthRequest): AuthResponse
    suspend fun registerCompany(request: CompanyRegisterRequest, logoBytes: ByteArray? = null): AuthResponse
    suspend fun registerDelivery(request: DeliveryRegisterRequest): AuthResponse
    suspend fun googleAuth(idToken: String): AuthResponse
    suspend fun googleAuthProgresive(request: GoogleAuthRequest): AuthResponse
    suspend fun logout(): MessageResponse
    suspend fun refreshToken(): MessageResponse
    suspend fun getProfile(): ProfileResponse
    suspend fun verifyToken(): ProfileResponse
    suspend fun registerFCMToken(token: String, userId: Int, fcmToken: String): MessageResponse
    suspend fun getCompanyById(id: Int): Company
    suspend fun getCompanyByUserId(token: String, userId: Int): Company
    suspend fun getCompanyProducts(id: Int): List<com.miltonvaz.voltio1.features.products.domain.entities.Product>
}
