package com.miltonvaz.voltio1.features.auth.domain.usecase

import com.miltonvaz.voltio1.features.auth.domain.repositories.IAuthRepository
import javax.inject.Inject

class RegisterFCMTokenUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(token: String, userId: Int, fcmToken: String): Result<Unit> {
        return try {
            repository.registerFCMToken(token, userId, fcmToken)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
