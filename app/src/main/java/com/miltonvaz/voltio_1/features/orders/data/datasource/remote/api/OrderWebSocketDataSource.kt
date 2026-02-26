package com.miltonvaz.voltio_1.features.orders.data.datasource.remote.api

import com.miltonvaz.voltio_1.core.network.VoltioSocketManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderWebSocketDataSource @Inject constructor(
    private val socketManager: VoltioSocketManager
) {
    fun observeLiveOrders(): Flow<String> {
        socketManager.connect()
        return socketManager.observeOrders()
    }

    fun disconnect() {
        socketManager.disconnect()
    }
}
