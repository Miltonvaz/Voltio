package com.miltonvaz.voltio_1.features.auth.domain.repositories

import com.ameth.voltio.features.login.data.datasource.remote.model.AuthResponse
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.AuthRequest
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.LoginRequest
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.MessageResponse
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.ProfileResponse

interface IAuthRepository {
    suspend fun login(request: LoginRequest): AuthResponse
    suspend fun register(request: AuthRequest): AuthResponse
    suspend fun logout(): MessageResponse
    suspend fun refreshToken(): MessageResponse
    suspend fun getProfile(): ProfileResponse
    suspend fun verifyToken(): ProfileResponse
}
