package com.miltonvaz.voltio_1.core.di

import com.miltonvaz.voltio_1.core.network.ISocketManager
import com.miltonvaz.voltio_1.core.network.VoltioSocketManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SocketModule {

    @Binds
    @Singleton
    abstract fun bindSocketManager(
        voltioSocketManager: VoltioSocketManager
    ): ISocketManager

    companion object {
        @Provides
        @Singleton
        fun provideSocket(@VoltioWebSocketUrl url: String): Socket {
            val opts = IO.Options().apply {
                transports = arrayOf("websocket")
            }
            return IO.socket(url, opts)
        }
    }
}
