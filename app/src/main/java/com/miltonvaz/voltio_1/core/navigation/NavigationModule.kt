    package com.miltonvaz.voltio_1.core.navigation

    import com.miltonvaz.voltio_1.features.products.di.navigation.ProductNavGraph
    import dagger.Binds
    import dagger.Module
    import dagger.hilt.InstallIn
    import dagger.hilt.components.SingletonComponent
    import dagger.multibindings.IntoSet

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class NavigationModule {

        @Binds
        @IntoSet
        abstract fun bindProductNavGraph(
            productNavGraph: ProductNavGraph
        ): FeatureNavGraph
    }