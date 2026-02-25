package com.miltonvaz.voltio_1.features.auth.di

import com.miltonvaz.voltio_1.core.network.VoltioApi
import com.miltonvaz.voltio_1.features.auth.data.repositories.AuthRepositoryImpl
import com.miltonvaz.voltio_1.features.auth.domain.repositories.IAuthRepository
import com.miltonvaz.voltio_1.features.auth.domain.usecase.AuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(api: VoltioApi): IAuthRepository = AuthRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideAuthUseCase(repo: IAuthRepository) = AuthUseCase(repo)
}