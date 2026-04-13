package com.miltonvaz.voltio1.features.company.domain.usecase

import android.util.Log
import com.miltonvaz.voltio1.features.auth.domain.entities.Company
import com.miltonvaz.voltio1.features.auth.domain.repositories.IAuthRepository
import javax.inject.Inject

class GetCompanyByUserIdUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(token: String, userId: Int): Result<Company> {
        return try {
            Log.d("GET_COMPANY_UC", "Llamando GET usuarios/$userId/empresa")
            val company = repository.getCompanyByUserId(token, userId)
            Log.d("GET_COMPANY_UC", "Respuesta OK: empresa=${company.id} (${company.commercialName})")
            Result.success(company)
        } catch (e: Exception) {
            Log.e("GET_COMPANY_UC", "Error al obtener empresa de userId=$userId: ${e.message}", e)
            Result.failure(e)
        }
    }
}
