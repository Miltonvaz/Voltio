package com.miltonvaz.voltio_1.features.directions.di

import com.miltonvaz.voltio_1.features.directions.data.repositories.DirectionRepositoryImpl
import com.miltonvaz.voltio_1.features.directions.domain.repositories.IDirectionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindDirectionRepository(
        directionRepositoryImpl: DirectionRepositoryImpl
    ): IDirectionRepository
}