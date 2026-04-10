package com.miltonvaz.voltio1.features.delivery.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.miltonvaz.voltio1.features.delivery.presentation.viewmodel.DeliveryViewModel
import com.miltonvaz.voltio1.features.orders.domain.entities.Order
import com.miltonvaz.voltio1.features.orders.domain.entities.OrderStatus
import com.miltonvaz.voltio1.features.products.presentation.components.AdminHeader

@Composable
fun DeliveryDashboardScreen(
    navController: NavHostController,
    onOrderClick: (Int) -> Unit,
    viewModel: DeliveryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onLocationPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val pendingOrders = uiState.assignedOrders.filter { it.status != OrderStatus.COMPLETED }
    val totalEarnings = uiState.assignedOrders.sumOf { it.totalAmount }
    val totalProducts = uiState.assignedOrders.sumOf { order -> order.products.sumOf { it.quantity } }

    Scaffold(
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AdminHeader(
                title = "Mis Entregas",
                subtitle = "Panel de repartidor",
                onBackClick = null,
                showProfile = true,
                showCart = false
            )

            Box(modifier = Modifier.weight(1f)) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF2563EB))
                    }
                } else if (uiState.assignedOrders.isEmpty()) {
                    EmptyDeliveryState(onRefresh = { viewModel.loadAssignedOrders() })
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                StatCard(
                                    modifier = Modifier.weight(1f),
                                    icon = Icons.Default.LocalShipping,
                                    value = "${pendingOrders.size}",
                                    label = "Pendientes",
                                    color = Color(0xFF2563EB),
                                    bgColor = Color(0xFFDBEAFE)
                                )
                                StatCard(
                                    modifier = Modifier.weight(1f),
                                    icon = Icons.Default.Inventory2,
                                    value = "$totalProducts",
                                    label = "Productos",
                                    color = Color(0xFF7C3AED),
                                    bgColor = Color(0xFFEDE9FE)
                                )
                                StatCard(
                                    modifier = Modifier.weight(1f),
                                    icon = Icons.Default.AttachMoney,
                                    value = "${"%.0f".format(totalEarnings)}",
                                    label = "Total MXN",
                                    color = Color(0xFF059669),
                                    bgColor = Color(0xFFD1FAE5)
                                )
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Pedidos asignados",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1E293B)
                                )
                                IconButton(onClick = { viewModel.loadAssignedOrders() }) {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "Actualizar",
                                        tint = Color(0xFF64748B)
                                    )
                                }
                            }
                        }

                        uiState.error?.let { error ->
                            item {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFFFE4E4)
                                ) {
                                    Text(
                                        text = error,
                                        modifier = Modifier.padding(12.dp),
                                        color = Color(0xFFDC2626),
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        itemsIndexed(pendingOrders, key = { _, order -> order.id }) { index, order ->
                            DeliveryOrderCard(
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
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    color: Color,
    bgColor: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
            Text(text = label, fontSize = 11.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun DeliveryOrderCard(order: Order, index: Int, onClick: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { 50 * (index + 1) },
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(400))
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = Color(0xFFDBEAFE)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = (order.clientName ?: "C").take(1).uppercase(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2563EB)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = order.clientName ?: "Cliente",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Pedido #${order.id}",
                                fontSize = 13.sp,
                                color = Color(0xFF94A3B8),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    val (statusText, statusColor, statusBg) = when (order.status) {
                        OrderStatus.PENDING -> Triple("Pendiente", Color(0xFFD97706), Color(0xFFFEF3C7))
                        OrderStatus.CONFIRMED -> Triple("Confirmado", Color(0xFF2563EB), Color(0xFFDBEAFE))
                        OrderStatus.IN_PROGRESS -> Triple("En camino", Color(0xFF7C3AED), Color(0xFFEDE9FE))
                        OrderStatus.COMPLETED -> Triple("Entregado", Color(0xFF059669), Color(0xFFD1FAE5))
                        OrderStatus.CANCELLED -> Triple("Cancelado", Color(0xFFDC2626), Color(0xFFFEE2E2))
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = statusBg
                    ) {
                        Text(
                            text = statusText,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = order.address ?: "Sin dirección",
                        fontSize = 13.sp,
                        color = Color(0xFF475569),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${order.products.sumOf { it.quantity }} productos",
                            fontSize = 13.sp,
                            color = Color(0xFF475569)
                        )
                    }
                    Text(
                        text = "${"%.2f".format(order.totalAmount)} MXN",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB),
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Iniciar entrega", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun EmptyDeliveryState(onRefresh: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.LocalShipping,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Color(0xFFCBD5E1)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Sin entregas pendientes",
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1E293B),
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Cuando se te asigne un pedido, aparecerá aquí automáticamente.",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onRefresh,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E7FF), contentColor = Color(0xFF4F46E5)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.height(50.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("ACTUALIZAR", fontWeight = FontWeight.Bold)
        }
    }
}