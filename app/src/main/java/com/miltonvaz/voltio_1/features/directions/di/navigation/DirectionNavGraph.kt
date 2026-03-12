package com.miltonvaz.voltio_1.features.directions.di.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.miltonvaz.voltio_1.core.navigation.Checkout
import com.miltonvaz.voltio_1.core.navigation.Directions
import com.miltonvaz.voltio_1.core.navigation.FeatureNavGraph
import com.miltonvaz.voltio_1.features.directions.presentation.screens.DirectionScreen
import javax.inject.Inject

class DirectionNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Directions> {
            DirectionScreen(
                onBackClick = { navController.popBackStack() },
                onContinueToCheckout = { navController.navigate(Checkout) }
            )
        }
    }
}