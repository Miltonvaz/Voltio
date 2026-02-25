package com.miltonvaz.voltio_1.features.orders.di.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.miltonvaz.voltio_1.core.navigation.*
import com.miltonvaz.voltio_1.features.orders.presentation.screens.*
import javax.inject.Inject

class OrdersNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Orders> {
            OrdersScreen(
                onBackClick = { navController.popBackStack() },
                onOrderClick = { orderId ->
                    navController.navigate(OrderDetailArg(orderId = orderId))
                }
            )
        }

        navGraphBuilder.composable<OrderDetailArg> {
            OrderDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        navGraphBuilder.composable<Checkout> {
            CheckoutScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = { navController.navigate(CheckoutAddress) }
            )
        }

        navGraphBuilder.composable<CheckoutAddress> {
            CheckoutAddressScreen(
                onBackClick = { navController.popBackStack() },
                onFinishOrder = {
                    navController.navigate(CheckoutResult(isSuccess = true)) {
                        popUpTo(Cart) { inclusive = true }
                    }
                }
            )
        }

        navGraphBuilder.composable<CheckoutResult> { backStackEntry ->
            val args = backStackEntry.toRoute<CheckoutResult>()
            CheckoutResultScreen(
                isSuccess = args.isSuccess,
                onFinish = {
                    navController.navigate(UserHome) {
                        popUpTo(UserHome) { inclusive = true }
                    }
                }
            )
        }
    }
}
