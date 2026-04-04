package com.miltonvaz.voltio1.features.auth.di.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.miltonvaz.voltio1.core.navigation.*
import com.miltonvaz.voltio1.features.auth.presentation.screens.LoginScreen
import com.miltonvaz.voltio1.features.auth.presentation.screens.register.RegisterScreen
import javax.inject.Inject

class AuthNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Login> {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterClick = { navController.navigate(Register) },
                onLoginSuccess = { user ->
                    val destination = when (user?.role) {
                        "admin", "company" -> AdminMenu
                        "delivery", "repartidor" -> DeliveryDashboard // <--- CORREGIDO: Acepta ambos términos
                        else -> UserHome
                    }
                    navController.navigate(destination) {
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
    }
}
