package com.miltonvaz.voltio_1.features.auth.domain.repositories

import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.*

interface IAuthRepository {
    suspend fun login(request: LoginRequest): AuthResponse
    suspend fun register(request: AuthRequest): AuthResponse
    suspend fun logout(): MessageResponse
    suspend fun refreshToken(): MessageResponse
    suspend fun getProfile(): ProfileResponse
    suspend fun verifyToken(): ProfileResponse
    suspend fun registerFCMToken(token: String, userId: Int, fcmToken: String): MessageResponse
}
