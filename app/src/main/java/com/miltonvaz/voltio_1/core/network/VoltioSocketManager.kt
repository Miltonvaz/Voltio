package com.miltonvaz.voltio_1.core.network

import android.util.Log
import com.miltonvaz.voltio_1.core.di.VoltioWebSocketUrl
import io.socket.client.IO
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
    @VoltioWebSocketUrl private val socketUrl: String
) {
    private var mSocket: Socket? = null
    private val TAG = "VoltioSocketManager"

    init {
        try {
            val opts = IO.Options()
            opts.transports = arrayOf("websocket") // Forzar WebSocket para mayor estabilidad
            
            Log.d(TAG, "Configurando Socket.IO para: $socketUrl")
            mSocket = IO.socket(socketUrl, opts)

            mSocket?.on(Socket.EVENT_CONNECT) {
                Log.d(TAG, "✅ CONECTADO exitosamente al servidor de Socket.IO")
            }

            mSocket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e(TAG, "❌ ERROR de conexión: ${args?.getOrNull(0)}")
            }

            mSocket?.on(Socket.EVENT_DISCONNECT) {
                Log.d(TAG, "⚠️ DESCONECTADO del servidor")
            }

            // Intentar conexión inicial
            mSocket?.connect()
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error fatal inicializando socket: ${e.message}")
        }
    }

    fun connect() {
        if (mSocket?.connected() == false) {
            Log.d(TAG, "Reintentando conexión...")
            mSocket?.connect()
        }
    }

    fun disconnect() {
        mSocket?.disconnect()
    }

    fun registerUser(userId: Int) {
        val data = JSONObject()
        data.put("id_usuario", userId)
        Log.d(TAG, "Emitiendo registrar_usuario para ID: $userId")
        mSocket?.emit("registrar_usuario", data)
    }

    fun observeOrders(): Flow<String> = callbackFlow {
        val listener = Emitter.Listener { args ->
            try {
                val response = args[0] as JSONObject
                Log.d(TAG, "📩 Notificación recibida: $response")
                
                // Extraemos los datos de la orden del campo 'datos' del JSON del servidor
                val orderData = response.optJSONObject("datos")
                if (orderData != null) {
                    trySend(orderData.toString())
                } else {
                    // Fallback: si envía la orden directamente
                    trySend(args[0].toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error procesando mensaje: ${e.message}")
                trySend(args[0].toString())
            }
        }

        // Escuchamos todos los eventos que emite tu servidor Node.js
        mSocket?.on("nueva_orden", listener)
        mSocket?.on("orden_actualizada", listener)
        mSocket?.on("orden_pendiente", listener)
        mSocket?.on("orden_entregada", listener)
        mSocket?.on("orden_cancelada", listener)

        awaitClose {
            mSocket?.off("nueva_orden", listener)
            mSocket?.off("orden_actualizada", listener)
            mSocket?.off("orden_pendiente", listener)
            mSocket?.off("orden_entregada", listener)
            mSocket?.off("orden_cancelada", listener)
        }
    }
}
