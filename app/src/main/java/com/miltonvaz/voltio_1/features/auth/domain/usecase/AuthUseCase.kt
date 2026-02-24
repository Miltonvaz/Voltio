package com.miltonvaz.voltio_1.features.auth.domain.usecase

import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.AuthRequest
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.AuthResponse
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.LoginRequest
import com.miltonvaz.voltio_1.features.auth.domain.repositories.IAuthRepository
import jakarta.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(loginRequest: LoginRequest): Result<AuthResponse> {
        return try {
            val response = repository.login(loginRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(authRequest: AuthRequest): Result<AuthResponse> {
        return try {
            val response = repository.register(authRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}