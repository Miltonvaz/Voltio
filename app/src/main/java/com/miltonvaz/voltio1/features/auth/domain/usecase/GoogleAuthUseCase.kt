package com.miltonvaz.voltio1.features.auth.domain.usecase

import com.miltonvaz.voltio1.features.auth.data.datasource.remote.model.AuthResponse
import com.miltonvaz.voltio1.features.auth.data.datasource.remote.model.GoogleAuthRequest
import com.miltonvaz.voltio1.features.auth.domain.repositories.IAuthRepository
import javax.inject.Inject

class GoogleAuthUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<AuthResponse> {
        return try {
            val response = repository.googleAuth(idToken)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerProgresive(request: GoogleAuthRequest): Result<AuthResponse> {
        return try {
            val response = repository.googleAuthProgresive(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
