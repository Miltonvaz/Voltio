package com.miltonvaz.voltio1.features.delivery.domain.repositories

import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.model.RepartidorDto
import com.miltonvaz.voltio1.features.delivery.domain.entities.DeliveryLocation
import kotlinx.coroutines.flow.Flow

interface IDeliveryRepository {
    fun observeLocationUpdates(): Flow<DeliveryLocation>
    suspend fun getLastKnownLocation(): DeliveryLocation?
    suspend fun startLocationTracking(orderId: Int)
    suspend fun stopLocationTracking(orderId: Int)
    suspend fun registerRepartidorInfo(token: String, userId: Int, vehicle: String, plates: String?): Result<Unit>
    suspend fun getAvailableDrivers(token: String): Result<List<RepartidorDto>>
}
