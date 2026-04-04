package com.miltonvaz.voltio1.features.delivery.domain.usecase

import com.miltonvaz.voltio1.features.delivery.domain.repositories.IDeliveryRepository
import javax.inject.Inject

class RegisterRepartidorInfoUseCase @Inject constructor(
    private val repository: IDeliveryRepository
) {
    suspend operator fun invoke(token: String, userId: Int, vehicle: String, plates: String?): Result<Unit> {
        return repository.registerRepartidorInfo(token, userId, vehicle, plates)
    }
}
