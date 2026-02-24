package com.miltonvaz.voltio_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.compose.AppTheme
import com.miltonvaz.voltio_1.core.di.AppContainer
import com.miltonvaz.voltio_1.core.navigation.NavigationWrapper
import com.miltonvaz.voltio_1.features.auth.di.AuthModule
import com.miltonvaz.voltio_1.features.auth.presentation.navigation.LoginNavGraph
import com.miltonvaz.voltio_1.features.products.di.ProductModule
import com.miltonvaz.voltio_1.features.products.presentation.navigation.ProductNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContainer = AppContainer(this)

        val authModule = AuthModule(appContainer)
        val productModule = ProductModule(appContainer)

        val navGraphs = listOf(
            LoginNavGraph(authModule.provideLoginViewModelFactory()),
            ProductNavGraph(productModule.provideProductViewModelFactory())
        )

        enableEdgeToEdge()
        setContent {
            AppTheme {
                NavigationWrapper(navGraphs = navGraphs)
            }
        }
    }
}