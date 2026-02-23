package com.miltonvaz.voltio_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.compose.AppTheme
import com.miltonvaz.voltio_1.core.navigation.NavigationWrapper
import com.miltonvaz.voltio_1.features.auth.presentation.navigation.LoginNavGraph
import com.miltonvaz.voltio_1.features.products.presentation.navigation.ProductNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navGraphs = listOf(
            LoginNavGraph(),
            ProductNavGraph()
        )

        enableEdgeToEdge()
        setContent {
            AppTheme {
                NavigationWrapper(navGraphs = navGraphs)
            }
        }
    }
}
