package com.miltonvaz.voltio_1.features.products.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.miltonvaz.voltio_1.core.navigation.Cart
import com.miltonvaz.voltio_1.core.navigation.UserHome
import com.miltonvaz.voltio_1.core.navigation.UserOrders

data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Any
)

@Composable
fun BottomNavBarClient(
    navController: NavHostController,
    selectedIndex: Int
) {
    val items = listOf(
        BottomNavItem("Inicio", Icons.Filled.Home, Icons.Outlined.Home, UserHome),
        BottomNavItem("Pedidos", Icons.AutoMirrored.Filled.ListAlt, Icons.AutoMirrored.Filled.ListAlt, UserOrders),
        BottomNavItem("Carrito", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart, Cart),
        BottomNavItem("Perfil", Icons.Filled.Person, Icons.Outlined.Person, UserHome)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFDDE8FF).copy(alpha = 0.95f))
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index
                IconButton(onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(UserHome) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }) {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = if (isSelected) Color(0xFF4F46E5) else Color(0xFF8A94A6),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}
