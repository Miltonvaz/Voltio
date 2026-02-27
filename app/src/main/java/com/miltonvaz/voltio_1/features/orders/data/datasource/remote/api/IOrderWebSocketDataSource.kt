package com.miltonvaz.voltio_1.features.orders.data.datasource.remote.api

import kotlinx.coroutines.flow.Flow

interface IOrderWebSocketDataSource {
    fun observeLiveOrders(): Flow<String>
    fun disconnect()
}
