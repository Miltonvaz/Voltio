package com.miltonvaz.voltio_1.core.di

import com.google.gson.Gson
import com.miltonvaz.voltio_1.core.network.VoltioApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @VoltioWebSocketUrl
    fun provideWebSocketUrl(): String {
        return "https://voltio-ws.ameth.shop"
    }

    @Provides
    @Singleton
    @VoltioRetrofit
    fun provideVoltioRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://voltio.ameth.shop/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideVoltioApi(@VoltioRetrofit retrofit: Retrofit): VoltioApi {
        return retrofit.create(VoltioApi::class.java)
    }
}
