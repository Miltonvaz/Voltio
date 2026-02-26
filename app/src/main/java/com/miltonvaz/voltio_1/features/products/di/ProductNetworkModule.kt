package com.miltonvaz.voltio_1.features.products.di

import com.miltonvaz.voltio_1.core.di.VoltioRetrofit
import com.miltonvaz.voltio_1.features.products.data.datasource.remote.ProductApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)

object ProductNetworkModule {
    @Provides
    @Singleton
    fun provideProductApiService(@VoltioRetrofit retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }
}