package com.miltonvaz.voltio_1.features.products.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.miltonvaz.voltio_1.core.navigation.AdminMenu
import com.miltonvaz.voltio_1.core.navigation.Inventory
import com.miltonvaz.voltio_1.core.navigation.Orders
import com.miltonvaz.voltio_1.core.navigation.Stock

@Composable
fun BottomNavBarAdmin(
    navController: NavHostController,
    selectedIndex: Int
) {
    val items = listOf(
        BottomNavItem("Resumen", Icons.Filled.Inventory2, Icons.Outlined.Inventory2, AdminMenu),
        BottomNavItem("Almacén", Icons.Filled.Inventory2, Icons.Outlined.Inventory2, Inventory),
        BottomNavItem("Pedidos", Icons.Filled.ListAlt, Icons.Outlined.ListAlt, Orders),
        BottomNavItem("Stock", Icons.Filled.BarChart, Icons.Outlined.BarChart, Stock)
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
                            popUpTo(AdminMenu) { saveState = true }
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
