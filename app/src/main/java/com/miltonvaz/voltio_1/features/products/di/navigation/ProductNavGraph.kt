package com.miltonvaz.voltio_1.features.products.di.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.miltonvaz.voltio_1.core.navigation.FeatureNavGraph
import com.miltonvaz.voltio_1.core.navigation.Home
import com.miltonvaz.voltio_1.core.navigation.ProductDetailArg
import com.miltonvaz.voltio_1.core.navigation.ProductFormArg
import com.miltonvaz.voltio_1.features.products.presentation.screens.AddProductScreen
import com.miltonvaz.voltio_1.features.products.presentation.screens.HomeScreen
import com.miltonvaz.voltio_1.features.products.presentation.screens.ProductDetailScreen
import com.miltonvaz.voltio_1.features.products.presentation.viewmodel.HomeViewModel
import javax.inject.Inject


class ProductNavGraph @Inject constructor() : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Home> { backStackEntry ->

            val viewModel: HomeViewModel = androidx.hilt.navigation.compose.hiltViewModel()

            val refreshNeeded by backStackEntry.savedStateHandle
                .getMutableStateFlow("refresh", false)
                .collectAsState()

            if (refreshNeeded) {
                viewModel.loadProducts()
                backStackEntry.savedStateHandle["refresh"] = false
            }

            HomeScreen(
                onAddProduct = {
                    navController.navigate(ProductFormArg(id = -1))
                },
                onEditProduct = { id ->
                    navController.navigate(ProductFormArg(id = id))
                },
                onProductClick = { id ->
                    navController.navigate(ProductDetailArg(id = id))
                }
            )
        }


        navGraphBuilder.composable<ProductFormArg> { backStackEntry ->

            val args = backStackEntry.toRoute<ProductFormArg>()

            AddProductScreen(
                onNavigateBack = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh", true)
                    navController.popBackStack()
                }
            )
        }

        navGraphBuilder.composable<ProductDetailArg> { backStackEntry ->
            val args = backStackEntry.toRoute<ProductDetailArg>()

            val viewModel: HomeViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val product = uiState.products.find { it.id == args.id }

            if (product != null) {
                ProductDetailScreen(
                    product = product,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}