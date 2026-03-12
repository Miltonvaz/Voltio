package com.miltonvaz.voltio_1.features.directions.di

import com.miltonvaz.voltio_1.core.di.VoltioRetrofit
import com.miltonvaz.voltio_1.features.directions.data.datasource.remote.api.DirectionApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object DirectionNetworkModule {

    @Provides
    @Singleton
    fun provideDirectionApiService(@VoltioRetrofit retrofit: Retrofit): DirectionApiService {
        return retrofit.create(DirectionApiService::class.java)
    }
}