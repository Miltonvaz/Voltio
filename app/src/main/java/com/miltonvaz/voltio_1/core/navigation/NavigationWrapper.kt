package com.miltonvaz.voltio_1.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationWrapper(
    navGraphs: List<FeatureNavGraph>,
    startDestination: Any = Login
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navGraphs.forEach { graph ->
            graph.registerGraph(this, navController)
        }
    }
}