package com.miltonvaz.voltio1.features.auth.domain.usecase

import com.miltonvaz.voltio1.features.auth.data.datasource.remote.model.*
import com.miltonvaz.voltio1.features.auth.domain.repositories.IAuthRepository
import javax.inject.Inject

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

    suspend fun registerCompany(request: CompanyRegisterRequest, logoBytes: ByteArray? = null): Result<AuthResponse> {
        return try {
            val response = repository.registerCompany(request, logoBytes)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerDelivery(request: DeliveryRegisterRequest): Result<AuthResponse> {
        return try {
            val response = repository.registerDelivery(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
