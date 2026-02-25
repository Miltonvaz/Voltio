package com.miltonvaz.voltio_1.features.orders.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.entities.OrderItem
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.OrderDetailUiState
import com.miltonvaz.voltio_1.features.orders.presentation.viewmodel.OrderDetailViewModel
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader
import java.util.Locale

@Composable
fun OrderDetailScreen(
    onBackClick: () -> Unit,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OrderDetailContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onStatusChange = { viewModel.updateStatus(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailContent(
    uiState: OrderDetailUiState,
    onBackClick: () -> Unit,
    onStatusChange: (String) -> Unit
) {
    var showStatusDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(Brush.verticalGradient(listOf(Color(0xFFE0E7FF), Color(0xFFC7D2FE))))
                    .padding(bottom = 24.dp)
            ) {
                Column {
                    IconButton(onClick = onBackClick, modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color(0xFF1E1B4B))
                    }
                    AdminHeader(
                        title = "Detalle de Pedido",
                        subtitle = uiState.order?.let { "Orden #${it.id}" } ?: "Cargando..."
                    )
                }
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF4F46E5))
                }
            } else if (uiState.order != null) {
                val order = uiState.order
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item {
                        StatusSection(order.status) { showStatusDialog = true }
                    }

                    item {
                        InfoSection(
                            title = "Información de Entrega",
                            icon = Icons.Default.LocalShipping,
                            content = order.address ?: "Sin dirección"
                        )
                    }

                    item {
                        InfoSection(
                            title = "Método de Pago",
                            icon = Icons.Default.Payments,
                            content = order.paymentType ?: "No especificado"
                        )
                    }

                    item {
                        Text(
                            text = "Productos",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1E293B)
                        )
                    }

                    items(order.products) { item ->
                        OrderItemRow(item)
                    }

                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1B4B))
                            Text(
                                text = "$${String.format(Locale.US, "%.2f", order.totalAmount)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF4F46E5)
                            )
                        }
                    }
                    
                    item { Spacer(modifier = Modifier.height(40.dp)) }
                }
            }
        }
    }

    if (showStatusDialog) {
        StatusUpdateDialog(
            currentStatus = uiState.order?.status ?: "",
            onDismiss = { showStatusDialog = false },
            onConfirm = { 
                onStatusChange(it)
                showStatusDialog = false
            }
        )
    }
}

@Composable
fun StatusSection(status: String, onEditClick: () -> Unit) {
    val statusColor = when (status.lowercase()) {
        "completado", "entregado" -> Color(0xFF10B981)
        "pendiente" -> Color(0xFFFBBF24)
        "cancelado" -> Color(0xFFEF4444)
        else -> Color.Gray
    }

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(statusColor))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Estado", fontSize = 12.sp, color = Color.Gray)
                    Text(status.uppercase(), fontWeight = FontWeight.ExtraBold, color = statusColor)
                }
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Cambiar Estado", tint = Color(0xFF4F46E5))
            }
        }
    }
}

@Composable
fun InfoSection(title: String, icon: ImageVector, content: String) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1B4B))
                Spacer(modifier = Modifier.height(4.dp))
                Text(content, fontSize = 14.sp, color = Color(0xFF64748B), lineHeight = 20.sp)
            }
        }
    }
}

@Composable
fun OrderItemRow(item: OrderItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFE0E7FF)
        ) {
            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.padding(12.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.productName ?: "Producto #${item.productId}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("${item.quantity} unidades x $${item.unitPrice}", fontSize = 13.sp, color = Color.Gray)
        }
        Text(
            "$${String.format(Locale.US, "%.2f", item.quantity * item.unitPrice)}",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E1B4B)
        )
    }
}

@Composable
fun StatusUpdateDialog(currentStatus: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    val statuses = listOf("pendiente", "entregado", "cancelado")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Actualizar Estado", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                statuses.forEach { status ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (status == currentStatus) Color(0xFFE0E7FF) else Color.Transparent)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (status == currentStatus), onClick = { onConfirm(status) })
                        Text(status.uppercase(), modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Preview(showBackground = true)
@Composable
fun OrderDetailPreview() {
    val dummyOrder = Order(
        id = 2,
        userId = 1,
        orderDate = "2026-02-25T01:36:33.000Z",
        status = "pendiente",
        totalAmount = 150.50,
        description = "Pedido urgente",
        address = "Av. Siempre Viva 123",
        paymentType = "tarjeta",
        products = listOf(
            OrderItem(1, "Sensor Ultrasónico HC-SR04", 1, 50.50),
            OrderItem(8, "Módulo Wi-Fi ESP32", 2, 50.00)
        )
    )
    OrderDetailContent(
        uiState = OrderDetailUiState(order = dummyOrder),
        onBackClick = {},
        onStatusChange = {}
    )
}
