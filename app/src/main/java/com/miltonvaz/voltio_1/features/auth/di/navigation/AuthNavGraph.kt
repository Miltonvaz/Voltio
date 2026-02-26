package com.miltonvaz.voltio_1.features.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.miltonvaz.voltio_1.core.navigation.AdminMenu
import com.miltonvaz.voltio_1.core.navigation.FeatureNavGraph
import com.miltonvaz.voltio_1.core.navigation.Login
import com.miltonvaz.voltio_1.core.navigation.Register
import com.miltonvaz.voltio_1.core.navigation.UserHome
import com.miltonvaz.voltio_1.features.auth.presentation.screens.LoginScreen
import com.miltonvaz.voltio_1.features.auth.presentation.screens.register.RegisterScreen
import javax.inject.Inject

class LoginNavGraph : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Login> {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterClick = { navController.navigate(Register) },
                onLoginSuccess = {
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }

        navGraphBuilder.composable<Register> {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Login) {
                        popUpTo(Register) { inclusive = true }
                    }
                }
            )
        }

        // Placeholder for future User Home
        navGraphBuilder.composable<UserHome> { /* TODO: User View */ }
    }
}
