package com.miltonvaz.voltio_1.features.orders.di.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.miltonvaz.voltio_1.core.navigation.*
import com.miltonvaz.voltio_1.features.orders.presentation.screens.*
import com.miltonvaz.voltio_1.features.orders.presentation.viewmodel.AdminOrdersViewModel
import com.miltonvaz.voltio_1.features.orders.presentation.viewmodel.UserOrdersViewModel
import javax.inject.Inject

class OrdersNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Cart> {
            CartScreen(
                navController = navController,
                onCheckoutClick = { navController.navigate(Checkout) }
            )
        }

        // Vista para el Admin: Ve todas las ordenes
        navGraphBuilder.composable<Orders> {
            val viewModel: AdminOrdersViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            
            OrdersScreen(
                navController = navController,
                uiState = uiState,
                isAdmin = true,
                onOrderClick = { orderId ->
                    navController.navigate(OrderDetailArg(orderId = orderId))
                }
            )
        }

        // Vista para el Cliente: Ve solo sus ordenes
        navGraphBuilder.composable<UserOrders> {
            val viewModel: UserOrdersViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            
            OrdersScreen(
                navController = navController,
                uiState = uiState,
                isAdmin = false,
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
