package com.miltonvaz.voltio_1.core.di

import com.miltonvaz.voltio_1.core.network.ISocketManager
import com.miltonvaz.voltio_1.core.network.VoltioSocketManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SocketModule {

    @Binds
    @Singleton
    abstract fun bindSocketManager(
        voltioSocketManager: VoltioSocketManager
    ): ISocketManager
}
