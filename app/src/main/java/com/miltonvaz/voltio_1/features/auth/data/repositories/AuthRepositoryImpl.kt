package com.miltonvaz.voltio_1.features.auth.data.repositories

import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.api.AuthApiService
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.AuthRequest
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.AuthResponse
import com.miltonvaz.voltio_1.core.network.VoltioApi
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.LoginRequest
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.MessageResponse
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.ProfileResponse
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
}
