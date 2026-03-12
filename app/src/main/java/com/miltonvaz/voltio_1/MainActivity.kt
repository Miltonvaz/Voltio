package com.miltonvaz.voltio_1

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.example.compose.AppTheme
import com.miltonvaz.voltio_1.core.navigation.NavigationWrapper
import com.miltonvaz.voltio_1.features.auth.di.navigation.AuthNavGraph
import com.miltonvaz.voltio_1.features.directions.di.navigation.DirectionNavGraph
import com.miltonvaz.voltio_1.features.orders.di.navigation.OrdersNavGraph
import com.miltonvaz.voltio_1.features.products.di.navigation.ProductNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var productNavGraph: ProductNavGraph

    @Inject
    lateinit var authNavGraph: AuthNavGraph

    @Inject
    lateinit var ordersNavGraph: OrdersNavGraph
    @Inject
    lateinit var directionNavGraph: DirectionNavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                NavigationWrapper(
                    navGraphs = listOf(authNavGraph, productNavGraph, ordersNavGraph, directionNavGraph)
                )
            }
        }
    }
}
