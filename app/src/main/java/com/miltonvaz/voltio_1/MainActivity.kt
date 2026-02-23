package com.miltonvaz.voltio_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.compose.AppTheme
import com.miltonvaz.voltio_1.core.navigation.NavigationWrapper
import com.miltonvaz.voltio_1.features.auth.presentation.navigation.LoginNavGraph
import com.miltonvaz.voltio_1.features.products.di.navigation.ProductNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var productNavGraph: ProductNavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                NavigationWrapper(
                    navGraphs = listOf(productNavGraph)
                )
            }
        }
    }
}
