package com.miltonvaz.voltio1.core.network

import kotlinx.coroutines.flow.Flow

interface ISocketManager {
    fun connect()
    fun disconnect()
    fun emit(event: String, data: Any)
    fun observeEvent(event: String): Flow<String>
    fun observeOrders(): Flow<String>
    
    // Nuevas funciones para Delivery
    fun joinOrderRoom(orderId: Int)
    fun leaveOrderRoom(orderId: Int)
    fun sendLocation(orderId: Int, lat: Double, lng: Double)
    fun observeLocation(): Flow<Pair<Double, Double>>
}
