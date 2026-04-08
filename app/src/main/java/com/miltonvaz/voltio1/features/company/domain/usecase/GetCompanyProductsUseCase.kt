package com.miltonvaz.voltio1.features.company.domain.usecase

import com.miltonvaz.voltio1.features.auth.domain.repositories.IAuthRepository
import com.miltonvaz.voltio1.features.products.domain.entities.Product
import javax.inject.Inject

class GetCompanyProductsUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(companyId: Int): Result<List<Product>> {
        return try {
            val products = repository.getCompanyProducts(companyId)
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
