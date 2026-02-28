package com.miltonvaz.voltio_1.features.orders.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.miltonvaz.voltio_1.core.navigation.Cart
import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.entities.OrderStatus
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.OrdersUiState
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader
import com.miltonvaz.voltio_1.features.products.presentation.components.BottomNavBarAdmin
import com.miltonvaz.voltio_1.features.products.presentation.components.BottomNavBarClient
import java.util.Locale

@Composable
fun OrdersScreen(
    navController: NavHostController,
    uiState: OrdersUiState,
    isAdmin: Boolean,
    onOrderClick: (Int) -> Unit
) {
    Scaffold(
        bottomBar = {
            if (isAdmin) {
                BottomNavBarAdmin(navController = navController, selectedIndex = 1)
            } else {
                BottomNavBarClient(navController = navController, selectedIndex = 1)
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AdminHeader(
                title = if (isAdmin) "Gestión de Pedidos" else "Mis Pedidos",
                subtitle = if (isAdmin) "Panel de administración" else "Seguimiento de tus compras",
                onBackClick = null,
                showProfile = true,
                showCart = !isAdmin,
                onCartClick = { if (!isAdmin) navController.navigate(Cart) }
            )

            Box(modifier = Modifier.weight(1f)) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF4F46E5))
                    }
                } else if (uiState.orders.isEmpty()) {
                    EmptyOrdersState()
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Text(
                                text = if (isAdmin) "Todos los pedidos" else "Historial de compras",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                            )
                        }
                        items(uiState.orders) { order ->
                            OrderItemCard(order = order, onClick = { onOrderClick(order.id) })
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderItemCard(order: Order, onClick: () -> Unit) {
    val statusColor = when (order.status) {
        OrderStatus.COMPLETED -> Color(0xFF10B981)
        OrderStatus.PENDING -> Color(0xFFF59E0B)
        OrderStatus.CANCELLED -> Color(0xFFEF4444)
        OrderStatus.CONFIRMED -> Color(0xFF3B82F6)
        OrderStatus.IN_PROGRESS -> Color(0xFF8B5CF6)
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Pedido #${order.id}", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFF1E1B4B))
                Surface(color = statusColor.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp)) {
                    Text(
                        text = order.status.apiValue.uppercase(Locale.getDefault()),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Column {
                    Text("Fecha de orden", fontSize = 11.sp, color = Color.Gray)
                    val displayDate = order.orderDate.split("T").firstOrNull() ?: order.orderDate
                    Text(displayDate, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                Text(
                    text = "$${String.format(Locale.US, "%.2f", order.totalAmount)}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color(0xFF4F46E5)
                )
            }
        }
    }
}

@Composable
fun EmptyOrdersState() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.ListAlt, null, modifier = Modifier.size(80.dp), tint = Color(0xFFE2E8F0))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Aún no tienes pedidos", fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), fontSize = 18.sp)
    }
}
