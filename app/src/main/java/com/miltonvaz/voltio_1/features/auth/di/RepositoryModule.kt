package com.miltonvaz.voltio_1.features.auth.di

import com.miltonvaz.voltio_1.features.auth.data.repositories.AuthRepositoryImpl
import com.miltonvaz.voltio_1.features.auth.domain.repositories.IAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): IAuthRepository
}
