package com.miltonvaz.voltio_1.features.orders.di

import com.miltonvaz.voltio_1.features.orders.data.repositories.OrderRepositoryImpl
import com.miltonvaz.voltio_1.features.orders.domain.repositories.IOrderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class OrderRepositoryModule {
    @Binds
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): IOrderRepository
}
