package com.miltonvaz.voltio_1.features.directions.di

import com.miltonvaz.voltio_1.features.directions.domain.repositories.IDirectionRepository
import com.miltonvaz.voltio_1.features.directions.domain.usecase.DirectionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DirectionModule {

    @Provides
    @Singleton
    fun provideDirectionUseCase(repo: IDirectionRepository) = DirectionUseCase(repo)
}