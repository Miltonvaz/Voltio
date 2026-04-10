package com.miltonvaz.voltio1.features.delivery.domain.usecase

import com.miltonvaz.voltio1.core.network.ISocketManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDriverLocationUseCase @Inject constructor(
    private val socketManager: ISocketManager
) {
    operator fun invoke(): Flow<Pair<Double, Double>> {
        return socketManager.observeLocation()
    }
}
