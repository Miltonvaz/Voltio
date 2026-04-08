package com.miltonvaz.voltio1.features.company.domain.usecase

import com.miltonvaz.voltio1.features.auth.domain.entities.Company
import com.miltonvaz.voltio1.features.auth.domain.repositories.IAuthRepository
import javax.inject.Inject

class GetCompanyByIdUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(companyId: Int): Result<Company> {
        return try {
            val company = repository.getCompanyById(companyId)
            Result.success(company)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
