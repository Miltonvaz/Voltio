package com.miltonvaz.voltio_1.features.orders.di

import com.miltonvaz.voltio_1.core.di.VoltioRetrofit
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.api.OrderApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}
