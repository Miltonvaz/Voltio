package com.miltonvaz.voltio_1.core.network

import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
}
