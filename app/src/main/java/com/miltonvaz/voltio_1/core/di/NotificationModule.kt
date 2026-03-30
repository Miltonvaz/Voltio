package com.miltonvaz.voltio_1.core.di

import com.google.firebase.messaging.FirebaseMessaging
import com.miltonvaz.voltio_1.core.notifications.data.repository.NotificationRepositoryImpl
import com.miltonvaz.voltio_1.core.notifications.domain.repository.INotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        firebaseMessaging: FirebaseMessaging
    ): INotificationRepository {
        return NotificationRepositoryImpl(firebaseMessaging)
    }
}
