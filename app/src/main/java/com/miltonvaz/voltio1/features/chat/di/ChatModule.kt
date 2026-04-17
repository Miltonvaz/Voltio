package com.miltonvaz.voltio1.features.chat.di

import com.miltonvaz.voltio1.features.chat.data.repositories.ChatRepositoryImpl
import com.miltonvaz.voltio1.features.chat.domain.repositories.IChatRepository
import com.miltonvaz.voltio1.features.chat.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): IChatRepository
}