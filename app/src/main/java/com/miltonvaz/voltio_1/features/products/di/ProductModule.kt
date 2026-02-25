package com.miltonvaz.voltio_1.features.products.di

import com.miltonvaz.voltio_1.features.products.data.datasource.remote.ProductApiService
import com.miltonvaz.voltio_1.features.products.data.repositories.ProductRepositoryImpl
import com.miltonvaz.voltio_1.features.products.domain.repositories.IProductRepository
import com.miltonvaz.voltio_1.features.products.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductModule {

    @Provides
    @Singleton
    fun provideProductRepository(api: ProductApiService): IProductRepository {
        return ProductRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetProductsUseCase(repo: IProductRepository) = GetProductsUseCase(repo)

    @Provides
    @Singleton
    fun provideGetProductByIdUseCase(repo: IProductRepository) = GetProductByIdUseCase(repo)

    @Provides
    @Singleton
    fun provideCreateProductUseCase(repo: IProductRepository) = CreateProductUseCase(repo)

    @Provides
    @Singleton
    fun provideUpdateProductUseCase(repo: IProductRepository) = UpdateProductUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteProductUseCase(repo: IProductRepository) = DeleteProductUseCase(repo)
}