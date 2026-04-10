package com.miltonvaz.voltio1.features.delivery.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.miltonvaz.voltio1.core.network.ISocketManager
import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.api.DeliveryApiService
import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.model.RepartidorRequest
import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.model.RepartidorDto
import com.miltonvaz.voltio1.features.delivery.domain.entities.DeliveryLocation
import com.miltonvaz.voltio1.features.delivery.domain.repositories.IDeliveryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DeliveryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val socketManager: ISocketManager,
    private val api: DeliveryApiService
) : IDeliveryRepository {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun observeLocationUpdates(): Flow<DeliveryLocation> = callbackFlow {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(3000)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location: Location ->
                    trySend(DeliveryLocation(location.latitude, location.longitude))
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    override suspend fun startLocationTracking(orderId: Int) {
        socketManager.joinOrderRoom(orderId)
    }

    override suspend fun stopLocationTracking(orderId: Int) {
        socketManager.leaveOrderRoom(orderId)
    }

    override suspend fun registerRepartidorInfo(token: String, userId: Int, vehicle: String, plates: String?): Result<Unit> {
        return try {
            api.createRepartidorInfo(token, RepartidorRequest(userId, vehicle, plates))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAvailableDrivers(token: String): Result<List<RepartidorDto>> {
        return try {
            val drivers = api.getAvailableDrivers("Bearer $token")
            Result.success(drivers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
