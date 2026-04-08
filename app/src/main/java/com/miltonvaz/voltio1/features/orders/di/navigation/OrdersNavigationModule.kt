package com.miltonvaz.voltio1.features.orders.di.navigation

import com.miltonvaz.voltio1.core.navigation.FeatureNavGraph
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class OrdersNavigationModule {

    @Binds
    @IntoSet
    abstract fun bindOrdersNavGraph(
        ordersNavGraph: OrdersNavGraph
    ): FeatureNavGraph
}