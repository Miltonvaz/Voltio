package com.miltonvaz.voltio_1.features.auth.di

import com.miltonvaz.voltio_1.core.di.VoltioRetrofit
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.api.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {

    @Provides
    @Singleton
    fun provideAuthApiService(@VoltioRetrofit retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}
