package com.miltonvaz.voltio1.features.chat.di

import com.miltonvaz.voltio1.core.di.VoltioRetrofit
import com.miltonvaz.voltio1.features.chat.data.datasource.remote.api.ChatApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatNetworkModule {

    @Provides
    @Singleton
    fun provideChatApiService(@VoltioRetrofit retrofit: Retrofit): ChatApiService =
        retrofit.create(ChatApiService::class.java)
}