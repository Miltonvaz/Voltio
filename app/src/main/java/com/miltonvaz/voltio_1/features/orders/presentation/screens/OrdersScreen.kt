package com.miltonvaz.voltio_1.features.orders.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.OrdersUiState
import com.miltonvaz.voltio_1.features.orders.presentation.viewmodel.OrdersViewModel
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader
import java.util.Locale

@Composable
fun OrdersScreen(
    onBackClick: () -> Unit = {},
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    OrdersScreenContent(
        uiState = uiState,
        onBackClick = onBackClick
    )
}

@Composable
fun OrdersScreenContent(
    uiState: OrdersUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header con el estilo Voltio Lab
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFE0E7FF), Color(0xFFC7D2FE))
                        )
                    )
                    .padding(bottom = 32.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick, modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color(0xFF1E1B4B))
                        }
                    }
                    AdminHeader(
                        title = "Mis Pedidos",
                        subtitle = "Seguimiento de tus compras en Voltio"
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    LoadingState()
                } else if (uiState.orders.isEmpty()) {
                    EmptyOrdersState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = "Actividad reciente",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                            )
                        }

                        items(uiState.orders) { order ->
                            OrderItemCard(order)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color(0xFF0F172A), strokeWidth = 4.dp)
    }
}

@Composable
private fun EmptyOrdersState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No tienes pedidos aún", fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
    }
}

@Composable
fun OrderItemCard(order: Order) {
    val statusColor = when (order.status.lowercase()) {
        "completado" -> Color(0xFF34D399)
        "pendiente" -> Color(0xFFFBBF24)
        "cancelado" -> Color(0xFFF87171)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #${order.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1E1B4B)
                )
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Text(
                            text = order.status,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = statusColor
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text("Fecha", fontSize = 11.sp, color = Color.Gray)
                    Text(order.orderDate, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Monto Total", fontSize = 11.sp, color = Color.Gray)
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
}

@Preview(showBackground = true)
@Composable
fun OrdersScreenPreview() {
    val dummyOrders = listOf(
        Order(1, 1, "2023-10-27 10:30", "Completado", 150.50, null, emptyList()),
        Order(2, 1, "2023-10-26 15:45", "Pendiente", 75.0, null, emptyList()),
        Order(3, 1, "2023-10-25 09:12", "Cancelado", 200.0, null, emptyList())
    )
    OrdersScreenContent(
        uiState = OrdersUiState(orders = dummyOrders, isLoading = false),
        onBackClick = {}
    )
}
