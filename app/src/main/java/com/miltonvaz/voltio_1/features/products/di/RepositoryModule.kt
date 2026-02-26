package com.miltonvaz.voltio_1.features.products.di


import com.miltonvaz.voltio_1.features.products.data.repositories.ProductRepositoryImpl
import com.miltonvaz.voltio_1.features.products.domain.repositories.IProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPostsRepository(
       producImplement : ProductRepositoryImpl
    ): IProductRepository
}

