package com.miltonvaz.voltio1.features.chat.di.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.miltonvaz.voltio1.core.navigation.ChatConversaciones
import com.miltonvaz.voltio1.core.navigation.ChatDetalle
import com.miltonvaz.voltio1.core.navigation.FeatureNavGraph
import com.miltonvaz.voltio1.features.chat.presentation.screens.ChatScreen
import com.miltonvaz.voltio1.features.chat.presentation.screens.ConversacionesScreen
import javax.inject.Inject

class ChatNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<ChatConversaciones> {
            ConversacionesScreen(
                onConversacionClick = { idConversacion, nombreUsuario ->
                    navController.navigate(ChatDetalle(idConversacion, nombreUsuario))
                }
            )
        }
        navGraphBuilder.composable<ChatDetalle> { backStackEntry ->
            val route = backStackEntry.toRoute<ChatDetalle>()
            ChatScreen(
                idConversacion = route.idConversacion,
                nombreUsuario = route.nombreUsuario,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}