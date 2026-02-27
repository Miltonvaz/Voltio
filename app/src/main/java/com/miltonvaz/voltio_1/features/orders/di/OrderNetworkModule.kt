package com.miltonvaz.voltio_1.features.orders.di

import com.miltonvaz.voltio_1.core.di.VoltioRetrofit
import com.miltonvaz.voltio_1.core.di.VoltioWebSocketUrl
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.api.IOrderWebSocketDataSource
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.api.OrderApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object OrderNetworkModule {

    @Provides
    @Singleton
    fun provideOrderApiService(@VoltioRetrofit retrofit: Retrofit): OrderApiService {
        return retrofit.create(OrderApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSocket(@VoltioWebSocketUrl url: String): Socket {
        val opts = IO.Options().apply {
            transports = arrayOf("websocket")
        }
        return IO.socket(url, opts)
    }

    @Provides
    @Singleton
    fun provideOrderWebSocketDataSource(socket: Socket): IOrderWebSocketDataSource {
        return object : IOrderWebSocketDataSource {
            override fun observeLiveOrders(): Flow<String> = callbackFlow {
                if (!socket.connected()) socket.connect()

                val listener = Emitter.Listener { args ->
                    try {
                        val response = args[0] as? JSONObject
                        val data = response?.optJSONObject("datos")?.toString() ?: args[0].toString()
                        trySend(data)
                    } catch (e: Exception) {
                        trySend(args[0].toString())
                    }
                }

                val events = listOf("nueva_orden", "orden_actualizada", "orden_pendiente", "orden_entregada", "orden_cancelada")
                events.forEach { socket.on(it, listener) }

                awaitClose {
                    events.forEach { socket.off(it, listener) }
                }
            }

            override fun disconnect() {
                socket.disconnect()
            }
        }
    }
}
