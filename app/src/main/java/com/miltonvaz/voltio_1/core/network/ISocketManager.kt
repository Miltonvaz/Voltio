package com.miltonvaz.voltio_1.core.network

import kotlinx.coroutines.flow.Flow

interface ISocketManager {
    fun connect()
    fun disconnect()
    fun emit(event: String, data: Any)
    fun observeEvent(event: String): Flow<String>
    fun observeOrders(): Flow<String>
}
