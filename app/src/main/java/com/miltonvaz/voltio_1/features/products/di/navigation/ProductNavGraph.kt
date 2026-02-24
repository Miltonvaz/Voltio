package com.miltonvaz.voltio_1.features.products.di.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.miltonvaz.voltio_1.core.navigation.*
import com.miltonvaz.voltio_1.features.products.presentation.screens.*
import com.miltonvaz.voltio_1.features.products.presentation.screens.MenuScreen
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
                onNavigateToInventory = { navController.navigate(Inventory) },
                onAddProduct = { navController.navigate(ProductFormArg(id = -1)) }
            )
        }

        navGraphBuilder.composable<Inventory> {
            HomeScreen(
                onAddProduct = { navController.navigate(ProductFormArg(id = -1)) },
                onEditProduct = { id -> navController.navigate(ProductFormArg(id = id)) },
                onProductClick = { id -> navController.navigate(ProductDetailArg(id = id)) }
            )
        }

        navGraphBuilder.composable<Stock> {
            HomeScreen(
                onAddProduct = { navController.navigate(ProductFormArg(id = -1)) },
                onEditProduct = { id -> navController.navigate(ProductFormArg(id = id)) },
                onProductClick = { id -> navController.navigate(ProductDetailArg(id = id)) }
            )
        }

        navGraphBuilder.composable<ProductFormArg> {
            AddProductScreen(
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

        navGraphBuilder.composable<Orders> { }
    }
}
