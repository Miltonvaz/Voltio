package com.miltonvaz.voltio1.features.delivery.domain.usecase

import com.miltonvaz.voltio1.features.delivery.domain.repositories.IDeliveryRepository
import javax.inject.Inject

class JoinOrderTrackingUseCase @Inject constructor(
    private val repository: IDeliveryRepository
) {
    suspend operator fun invoke(orderId: Int) {
        repository.startLocationTracking(orderId)
    }
}
