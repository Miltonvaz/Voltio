package com.miltonvaz.voltio1.features.delivery.di

import android.content.Context
import com.miltonvaz.voltio1.core.di.VoltioRetrofit
import com.miltonvaz.voltio1.core.network.ISocketManager
import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.api.DeliveryApiService
import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.api.GoogleDirectionsApiService
import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.api.OpenRouteService
import com.miltonvaz.voltio1.features.delivery.data.repository.DeliveryRepositoryImpl
import com.miltonvaz.voltio1.features.delivery.domain.repositories.IDeliveryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeliveryModule {

    @Provides
    @Singleton
    fun provideDeliveryApiService(@VoltioRetrofit retrofit: Retrofit): DeliveryApiService {
        return retrofit.create(DeliveryApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGoogleDirectionsApiService(@VoltioRetrofit retrofit: Retrofit): GoogleDirectionsApiService {
        return retrofit.create(GoogleDirectionsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenRouteService(@VoltioRetrofit retrofit: Retrofit): OpenRouteService {
        return retrofit.create(OpenRouteService::class.java)
    }

    @Provides
    @Singleton
    fun provideDeliveryRepository(
        @ApplicationContext context: Context,
        socketManager: ISocketManager,
        api: DeliveryApiService
    ): IDeliveryRepository {
        return DeliveryRepositoryImpl(context, socketManager, api)
    }
}
