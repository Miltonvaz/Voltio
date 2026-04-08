package com.miltonvaz.voltio1.features.company.domain.usecase

import com.miltonvaz.voltio1.features.auth.domain.entities.Company
import com.miltonvaz.voltio1.features.auth.domain.repositories.IAuthRepository
import javax.inject.Inject

class GetCompanyByUserIdUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(token: String, userId: Int): Result<Company> {
        return try {
            val company = repository.getCompanyByUserId(token, userId)
            Result.success(company)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
