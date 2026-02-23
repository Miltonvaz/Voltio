package com.miltonvaz.voltio_1.features.products.di

import com.miltonvaz.voltio_1.core.di.AppContainer
import com.miltonvaz.voltio_1.features.products.data.datasource.remote.ProductApiService
import com.miltonvaz.voltio_1.features.products.data.repositories.ProductRepositoryImpl
import com.miltonvaz.voltio_1.features.products.domain.usecase.*
import com.miltonvaz.voltio_1.features.products.presentation.viewmodel.viewModelFactory.ProductViewModelFactory

class ProductModule(private val appContainer: AppContainer) {

    private val productApiService: ProductApiService by lazy {
        appContainer.retrofit.create(ProductApiService::class.java)
    }

    private val productRepository by lazy {
        ProductRepositoryImpl(productApiService)
    }

    private val getProductsUseCase by lazy { GetProductsUseCase(productRepository) }
    private val deleteProductUseCase by lazy { DeleteProductUseCase(productRepository) }
    private val getProductByIdUseCase by lazy { GetProductByIdUseCase(productRepository) }
    private val createProductUseCase by lazy { CreateProductUseCase(productRepository) }
    private val updateProductUseCase by lazy { UpdateProductUseCase(productRepository) }

    fun provideProductViewModelFactory(productId: Int = -1): ProductViewModelFactory {
        return ProductViewModelFactory(
            productId = productId,
            getProductsUseCase = getProductsUseCase,
            getProductByIdUseCase = getProductByIdUseCase,
            createProductUseCase = createProductUseCase,
            updateProductUseCase = updateProductUseCase,
            deleteProductUseCase = deleteProductUseCase,
            tokenManager = appContainer.sessionManager
        )
    }
}