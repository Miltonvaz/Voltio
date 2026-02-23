package com.miltonvaz.voltio_1.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @JsonPlaceHolderRetrofit
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @VoltioRetrofit
    fun provideVoltioRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://voltio.ameth.shop/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}