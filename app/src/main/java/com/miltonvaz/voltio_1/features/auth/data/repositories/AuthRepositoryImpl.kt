package com.miltonvaz.voltio_1.features.auth.data.repositories

import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.*
import com.miltonvaz.voltio_1.core.network.VoltioApi
import com.miltonvaz.voltio_1.features.auth.domain.repositories.IAuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: VoltioApi
) : IAuthRepository {

    override suspend fun login(loginRequest: LoginRequest): AuthResponse {
        return api.login(loginRequest)
    }

    override suspend fun register(authRequest: AuthRequest): AuthResponse {
        return api.register(authRequest)
    }

    override suspend fun logout(): MessageResponse {
        return api.logout()
    }

    override suspend fun refreshToken(): MessageResponse {
        return api.refreshToken()
    }

    override suspend fun getProfile(): ProfileResponse {
        return api.getProfile()
    }

    override suspend fun verifyToken(): ProfileResponse {
        return api.verifyToken()
    }

    override suspend fun registerFCMToken(token: String, userId: Int, fcmToken: String): MessageResponse {
        return api.registerFCMToken("Bearer $token", RegisterFCMTokenRequest(userId, fcmToken))
    }
}
