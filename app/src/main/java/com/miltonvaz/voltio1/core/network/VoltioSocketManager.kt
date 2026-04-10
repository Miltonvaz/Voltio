package com.miltonvaz.voltio1.core.network

import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoltioSocketManager @Inject constructor(
    private val socket: Socket
) : ISocketManager {

    override fun connect() {
        if (!socket.connected()) {
            socket.connect()
        }
    }

    override fun disconnect() {
        socket.disconnect()
    }

    override fun emit(event: String, data: Any) {
        socket.emit(event, data)
    }

    override fun observeEvent(event: String): Flow<String> = callbackFlow {
        val listener = Emitter.Listener { args ->
            if (args.isNotEmpty()) {
                trySend(args[0].toString())
            }
        }
        socket.on(event, listener)
        awaitClose {
            socket.off(event, listener)
        }
    }

    override fun observeOrders(): Flow<String> = callbackFlow {
        val listener = Emitter.Listener { args ->
            if (args.isNotEmpty()) {
                trySend(args[0].toString())
            }
        }
        
        val events = listOf(
            "nueva_orden", 
            "orden_actualizada", 
            "orden_pendiente", 
            "orden_entregada", 
            "orden_cancelada"
        )
        
        events.forEach { socket.on(it, listener) }
        
        awaitClose {
            events.forEach { socket.off(it, listener) }
        }
    }

    override fun joinOrderRoom(orderId: Int) {
        socket.emit("join_order", orderId)
    }

    override fun leaveOrderRoom(orderId: Int) {
        socket.emit("leave_order", orderId)
    }

    override fun sendLocation(orderId: Int, lat: Double, lng: Double) {
        val data = JSONObject().apply {
            put("orderId", orderId)
            put("lat", lat)
            put("lng", lng)
        }
        socket.emit("update_location", data)
    }

    override fun observeLocation(): Flow<Pair<Double, Double>> = callbackFlow {
        val listener = Emitter.Listener { args ->
            if (args.isNotEmpty()) {
                try {
                    val data = args[0] as JSONObject
                    val lat = data.getDouble("lat")
                    val lng = data.getDouble("lng")
                    trySend(lat to lng)
                } catch (e: Exception) {
                    // Log error or ignore invalid data
                }
            }
        }
        socket.on("location_changed", listener)
        awaitClose {
            socket.off("location_changed", listener)
        }
    }
}
