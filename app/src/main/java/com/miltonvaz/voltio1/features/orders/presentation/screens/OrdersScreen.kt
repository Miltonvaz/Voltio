package com.miltonvaz.voltio1.features.orders.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.miltonvaz.voltio1.core.navigation.Cart
import com.miltonvaz.voltio1.features.orders.domain.entities.Order
import com.miltonvaz.voltio1.features.orders.domain.entities.OrderStatus
import com.miltonvaz.voltio1.features.orders.presentation.screens.UiState.OrdersUiState
import com.miltonvaz.voltio1.features.products.presentation.components.AdminHeader
import com.miltonvaz.voltio1.features.products.presentation.components.BottomNavBarAdmin
import com.miltonvaz.voltio1.features.products.presentation.components.BottomNavBarClient
import java.util.Locale

@Composable
fun OrdersScreen(
    navController: NavHostController,
    uiState: OrdersUiState,
    isAdmin: Boolean,
    onOrderClick: (Int) -> Unit
) {
    var selectedFilter by remember { mutableStateOf<OrderStatus?>(null) }

    val filteredOrders = if (selectedFilter == null) {
        uiState.orders
    } else {
        uiState.orders.filter { it.status == selectedFilter }
    }

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
                    OrdersLoadingState()
                } else if (uiState.orders.isEmpty()) {
                    EmptyOrdersState()
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 20.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Order count summary
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isAdmin) "Todos los pedidos" else "Historial reciente",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1E293B)
                                )
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFFE8EEFF)
                                ) {
                                    Text(
                                        text = "${filteredOrders.size} pedidos",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4F46E5)
                                    )
                                }
                            }
                        }

                        // Filter chips
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OrderFilterChip(
                                    label = "Todos",
                                    isSelected = selectedFilter == null,
                                    color = Color(0xFF4F46E5),
                                    onClick = { selectedFilter = null }
                                )
                                OrderFilterChip(
                                    label = "Pendientes",
                                    isSelected = selectedFilter == OrderStatus.PENDING,
                                    color = Color(0xFFF59E0B),
                                    count = uiState.orders.count { it.status == OrderStatus.PENDING },
                                    onClick = { selectedFilter = OrderStatus.PENDING }
                                )
                                if (isAdmin) {
                                    OrderFilterChip(
                                        label = "Confirmados",
                                        isSelected = selectedFilter == OrderStatus.CONFIRMED,
                                        color = Color(0xFF3B82F6),
                                        count = uiState.orders.count { it.status == OrderStatus.CONFIRMED },
                                        onClick = { selectedFilter = OrderStatus.CONFIRMED }
                                    )
                                }
                                OrderFilterChip(
                                    label = "En curso",
                                    isSelected = selectedFilter == OrderStatus.IN_PROGRESS,
                                    color = Color(0xFF8B5CF6),
                                    count = uiState.orders.count { it.status == OrderStatus.IN_PROGRESS },
                                    onClick = { selectedFilter = OrderStatus.IN_PROGRESS }
                                )
                                OrderFilterChip(
                                    label = "Completados",
                                    isSelected = selectedFilter == OrderStatus.COMPLETED,
                                    color = Color(0xFF10B981),
                                    count = uiState.orders.count { it.status == OrderStatus.COMPLETED },
                                    onClick = { selectedFilter = OrderStatus.COMPLETED }
                                )
                                OrderFilterChip(
                                    label = "Cancelados",
                                    isSelected = selectedFilter == OrderStatus.CANCELLED,
                                    color = Color(0xFFEF4444),
                                    count = uiState.orders.count { it.status == OrderStatus.CANCELLED },
                                    onClick = { selectedFilter = OrderStatus.CANCELLED }
                                )
                            }
                        }

                        itemsIndexed(filteredOrders) { index, order ->
                            AnimatedOrderItem(
                                order = order,
                                index = index,
                                onClick = { onOrderClick(order.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderFilterChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    count: Int? = null,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color.copy(alpha = 0.15f) else Color.White,
        shadowElevation = if (isSelected) 0.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) color else Color(0xFF64748B)
            )
            if (count != null && count > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = CircleShape,
                    color = color.copy(alpha = if (isSelected) 0.2f else 0.1f),
                    modifier = Modifier.size(20.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "$count",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrdersLoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val infiniteTransition = rememberInfiniteTransition(label = "loading")
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "scale"
        )
        CircularProgressIndicator(
            modifier = Modifier.scale(scale),
            color = Color(0xFF455E91),
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun AnimatedOrderItem(order: Order, index: Int, onClick: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { 50 * (index + 1) },
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(400))
    ) {
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
            OrderItemCard(order = order, onClick = onClick)
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

    val statusLabel = when (order.status) {
        OrderStatus.COMPLETED -> "Entregado"
        OrderStatus.PENDING -> "Pendiente"
        OrderStatus.CANCELLED -> "Cancelado"
        OrderStatus.CONFIRMED -> "Confirmado"
        OrderStatus.IN_PROGRESS -> "En camino"
    }

    val isPendingAction = order.status == OrderStatus.PENDING || order.status == OrderStatus.IN_PROGRESS

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono Izquierdo Estilizado
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(statusColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Información Central
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Pedido #${order.id}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color(0xFF1E1B4B)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Status Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isPendingAction) {
                            val infiniteTransition = rememberInfiniteTransition(label = "badgePulse")
                            val pulseScale by infiniteTransition.animateFloat(
                                initialValue = 0.7f,
                                targetValue = 1.1f,
                                animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
                                label = "pulse"
                            )
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(statusColor)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(statusColor)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(6.dp))
                        
                        Text(
                            text = statusLabel,
                            color = statusColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Text(
                    text = order.orderDate.split("T").firstOrNull() ?: order.orderDate,
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Medium
                )
            }

            // Precio y Flecha derecha
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${String.format(Locale.US, "%.2f", order.totalAmount)}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = Color(0xFF1E1B4B)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color(0xFFCBD5E1),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyOrdersState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = Color(0xFFF1F5F9)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.ListAlt, null, modifier = Modifier.size(44.dp), tint = Color(0xFFD1D9E6))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Sin pedidos activos",
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1E293B),
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Aquí aparecerá el seguimiento de tus pedidos",
            color = Color(0xFF94A3B8),
            fontSize = 14.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
