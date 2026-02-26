package com.miltonvaz.voltio_1.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.miltonvaz.voltio_1.features.auth.presentation.navigation.LoginNavGraph
import com.miltonvaz.voltio_1.features.products.presentation.navigation.ProductNavGraph

@Composable
fun NavigationWrapper(startDestination: Any = HomeClient) {
    val navController = rememberNavController()
    val navGraphs = listOf(LoginNavGraph(), ProductNavGraph())

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navGraphs.forEach { graph ->
            graph.registerGraph(this, navController)
        }
    }
}