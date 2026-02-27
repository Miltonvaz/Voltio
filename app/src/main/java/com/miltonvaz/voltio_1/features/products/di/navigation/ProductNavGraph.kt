package com.miltonvaz.voltio_1.features.products.di.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.miltonvaz.voltio_1.core.navigation.*
import com.miltonvaz.voltio_1.features.products.presentation.screens.*
import com.miltonvaz.voltio_1.features.products.presentation.viewmodel.HomeViewModel
import com.miltonvaz.voltio_1.features.products.presentation.viewmodel.ProductFormViewModel
import javax.inject.Inject

class ProductNavGraph @Inject constructor() : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<AdminMenu> {
            MenuScreen(
                onNavigateToOrders = { navController.navigate(Orders) },
                onNavigateToStock = { navController.navigate(Stock) },
                onNavigateToInventory = { navController.navigate(Inventory) }
            )
        }

        navGraphBuilder.composable<Inventory> { backStackEntry ->
            val viewModel: HomeViewModel = hiltViewModel()

            val refreshNeeded by backStackEntry.savedStateHandle
                .getMutableStateFlow("refresh", false)
                .collectAsState()

            if (refreshNeeded) {
                viewModel.loadProducts()
                backStackEntry.savedStateHandle["refresh"] = false
            }

            HomeScreen(
                viewModel = viewModel,
                onAddProduct = { navController.navigate(ProductFormArg(id = -1)) },
                onEditProduct = { id -> navController.navigate(ProductFormArg(id = id)) },
                onProductClick = { id -> navController.navigate(ProductDetailArg(id = id)) }
            )
        }

        navGraphBuilder.composable<UserHome> {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreenClient(
                navController = navController,
                viewModel = viewModel,
                onProductClick = { id -> navController.navigate(ProductDetailClientArg(id = id)) },
                onCartClick = { navController.navigate(Cart) }
            )
        }

        navGraphBuilder.composable<Stock> {
            StockScreen(
                onBackClick = { navController.popBackStack() },
                onAddProduct = { navController.navigate(ProductFormArg(id = -1)) },
                onEditProduct = { id -> navController.navigate(ProductFormArg(id = id)) }
            )
        }

        navGraphBuilder.composable<ProductFormArg> {
            val viewModel: ProductFormViewModel = hiltViewModel()

            AddProductScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh", true)
                    navController.popBackStack()
                }
            )
        }

        navGraphBuilder.composable<ProductDetailArg> { backStackEntry ->
            val args = backStackEntry.toRoute<ProductDetailArg>()
            ProductDetailScreen(
                productId = args.id,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        navGraphBuilder.composable<ProductDetailClientArg> { backStackEntry ->
            val args = backStackEntry.toRoute<ProductDetailClientArg>()
            val homeViewModel: HomeViewModel = hiltViewModel()
            val uiState by homeViewModel.uiState.collectAsState()

            val product = uiState.products.find { it.id == args.id }
            if (product != null) {
                ProductDetailScreenClient(
                    navController = navController,
                    product = product,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCart = { navController.navigate(Cart) }
                )
            }
        }
    }
}
