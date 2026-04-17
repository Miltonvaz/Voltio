package com.miltonvaz.voltio1.features.delivery.di.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.miltonvaz.voltio1.core.navigation.*
import com.miltonvaz.voltio1.features.delivery.presentation.screens.DeliveryDashboardScreen
import com.miltonvaz.voltio1.features.delivery.presentation.screens.DeliveryTrackingScreen
import com.miltonvaz.voltio1.features.delivery.presentation.screens.UserTrackingScreen
import javax.inject.Inject

class DeliveryNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<DeliveryDashboard> {
            DeliveryDashboardScreen(
                navController = navController,
                onOrderClick = { orderId ->
                    navController.navigate(DeliveryTracking(orderId))
                }
            )
        }

        navGraphBuilder.composable<DeliveryTracking> { backStackEntry ->
            val args = backStackEntry.toRoute<DeliveryTracking>()
            DeliveryTrackingScreen(
                orderId = args.orderId,
                onBackClick = { navController.popBackStack() }
            )
        }

        navGraphBuilder.composable<UserTracking> { backStackEntry ->
            val args = backStackEntry.toRoute<UserTracking>()
            UserTrackingScreen(
                orderId = args.orderId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
