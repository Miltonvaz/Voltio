package com.miltonvaz.voltio_1.features.auth.di

import com.miltonvaz.voltio_1.features.auth.data.repositories.AuthRepositoryImpl
import com.miltonvaz.voltio_1.features.auth.domain.repositories.IAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): IAuthRepository
}