package com.miltonvaz.voltio1.features.delivery.domain.usecase

import com.miltonvaz.voltio1.core.network.ISocketManager
import javax.inject.Inject

class SendLocationUseCase @Inject constructor(
    private val socketManager: ISocketManager
) {
    operator fun invoke(orderId: Int, lat: Double, lng: Double) {
        socketManager.sendLocation(orderId, lat, lng)
    }
}
