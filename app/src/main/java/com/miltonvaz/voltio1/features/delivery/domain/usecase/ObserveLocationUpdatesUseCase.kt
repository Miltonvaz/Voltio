package com.miltonvaz.voltio1.features.delivery.domain.usecase

import com.miltonvaz.voltio1.features.delivery.domain.entities.DeliveryLocation
import com.miltonvaz.voltio1.features.delivery.domain.repositories.IDeliveryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLocationUpdatesUseCase @Inject constructor(
    private val repository: IDeliveryRepository
) {
    operator fun invoke(): Flow<DeliveryLocation> {
        return repository.observeLocationUpdates()
    }

    suspend fun getLastKnownLocation(): DeliveryLocation? {
        return repository.getLastKnownLocation()
    }
}
