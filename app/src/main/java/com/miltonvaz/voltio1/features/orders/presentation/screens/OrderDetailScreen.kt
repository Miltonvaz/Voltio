package com.miltonvaz.voltio1.features.orders.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.model.RepartidorDto
import com.miltonvaz.voltio1.features.orders.domain.entities.Order
import com.miltonvaz.voltio1.features.orders.domain.entities.OrderItem
import com.miltonvaz.voltio1.features.orders.domain.entities.OrderStatus
import com.miltonvaz.voltio1.features.orders.presentation.screens.UiState.OrderDetailUiState
import com.miltonvaz.voltio1.features.orders.presentation.viewmodel.OrderDetailViewModel
import com.miltonvaz.voltio1.features.products.presentation.components.AdminHeader
import java.util.Locale

@Composable
fun OrderDetailScreen(
    onBackClick: () -> Unit,
    isAdmin: Boolean = false,
    onTrackClick: (Int) -> Unit = {},
    viewModel: OrderDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val availableDrivers by viewModel.availableDrivers.collectAsStateWithLifecycle()

    OrderDetailContent(
        uiState = uiState,
        isAdmin = isAdmin,
        availableDrivers = availableDrivers,
        onBackClick = onBackClick,
        onTrackClick = onTrackClick,
        onStatusChange = { viewModel.updateStatus(it) },
        onAssignDriver = { viewModel.assignOrderToDriver(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailContent(
    uiState: OrderDetailUiState,
    isAdmin: Boolean,
    availableDrivers: List<RepartidorDto>,
    onBackClick: () -> Unit,
    onTrackClick: (Int) -> Unit = {},
    onStatusChange: (OrderStatus) -> Unit,
    onAssignDriver: (Int) -> Unit
) {
    var showStatusDialog by remember { mutableStateOf(false) }
    var showDriverDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AdminHeader(
                title = "Detalle de Pedido",
                subtitle = uiState.order?.let { "Orden #${it.id}" } ?: "Cargando...",
                onBackClick = onBackClick,
                showProfile = false,
                showCart = false
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF455E91))
                }
            } else if (uiState.order != null) {
                val order = uiState.order
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Order Progress Timeline
                    item {
                        OrderProgressTimeline(status = order.status)
                    }

                    item {
                        StatusSection(
                            status = order.status,
                            showEdit = isAdmin,
                            onEditClick = { showStatusDialog = true }
                        )
                    }

                    item {
                        DeliverySection(
                            order = order,
                            isAdmin = isAdmin,
                            onAssignClick = { showDriverDialog = true }
                        )
                    }

                    // Botón "Seguir pedido" — solo para clientes cuando el pedido está en camino
                    if (!isAdmin && order.status == OrderStatus.IN_PROGRESS) {
                        item {
                            Button(
                                onClick = { onTrackClick(order.id) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF455E91)
                                )
                            ) {
                                Icon(
                                    Icons.Default.LocalShipping,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Seguir pedido en vivo",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Productos",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1E293B)
                            )
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFE0E7FF)
                            ) {
                                Text(
                                    text = "${order.products.size} items",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF455E91)
                                )
                            }
                        }
                    }

                    items(order.products) { item ->
                        OrderItemRow(item)
                    }

                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFE2E8F0))
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFE0E7FF)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1B4B))
                                Text(
                                    text = "$${String.format(Locale.US, "%.2f", order.totalAmount)} MXN",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF455E91)
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
        }
    }

    if (showStatusDialog) {
        StatusUpdateDialog(
            currentStatus = uiState.order?.status ?: OrderStatus.PENDING,
            onDismiss = { showStatusDialog = false },
            onConfirm = {
                onStatusChange(it)
                showStatusDialog = false
            }
        )
    }

    if (showDriverDialog) {
        DriverAssignmentDialog(
            drivers = availableDrivers,
            onDismiss = { showDriverDialog = false },
            onConfirm = {
                onAssignDriver(it)
                showDriverDialog = false
            }
        )
    }
}

@Composable
fun OrderProgressTimeline(status: OrderStatus) {
    val steps = listOf(
        "Pendiente" to OrderStatus.PENDING,
        "Confirmado" to OrderStatus.CONFIRMED,
        "En camino" to OrderStatus.IN_PROGRESS,
        "Entregado" to OrderStatus.COMPLETED
    )

    val currentStepIndex = when (status) {
        OrderStatus.PENDING -> 0
        OrderStatus.CONFIRMED -> 1
        OrderStatus.IN_PROGRESS -> 2
        OrderStatus.COMPLETED -> 3
        OrderStatus.CANCELLED -> -1
    }

    if (status == OrderStatus.CANCELLED) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFFEE2E2)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Cancel, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Pedido Cancelado", fontWeight = FontWeight.ExtraBold, color = Color(0xFFEF4444), fontSize = 16.sp)
            }
        }
        return
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Progreso del pedido", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { index, (label, _) ->
                    val isActive = index <= currentStepIndex
                    val activeColor = Color(0xFF455E91)
                    val inactiveColor = Color(0xFFE2E8F0)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (isActive) activeColor else inactiveColor),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isActive && index < currentStepIndex) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isActive) Color.White else Color(0xFF94A3B8)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                            color = if (isActive) activeColor else Color(0xFF94A3B8)
                        )
                    }
                }
            }
            // Progress bar
            Spacer(modifier = Modifier.height(12.dp))
            val progress = if (currentStepIndex >= 0) (currentStepIndex + 1) / steps.size.toFloat() else 0f
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFE2E8F0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF455E91))
                )
            }
        }
    }
}

@Composable
fun DeliverySection(order: Order, isAdmin: Boolean, onAssignClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE0E7FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF455E91), modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text("Repartidor", fontSize = 12.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
                    Text(
                        text = order.deliveryPersonName ?: "No asignado",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (order.deliveryPersonName != null) Color(0xFF1E1B4B) else Color(0xFFEF4444)
                    )
                }
            }
            if (isAdmin) {
                FilledTonalButton(
                    onClick = onAssignClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFFE0E7FF),
                        contentColor = Color(0xFF455E91)
                    )
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Asignar", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Asignar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun StatusSection(status: OrderStatus, showEdit: Boolean, onEditClick: () -> Unit) {
    val statusColor = when (status) {
        OrderStatus.COMPLETED -> Color(0xFF10B981)
        OrderStatus.PENDING -> Color(0xFFFBBF24)
        OrderStatus.CANCELLED -> Color(0xFFEF4444)
        OrderStatus.CONFIRMED -> Color(0xFF3B82F6)
        OrderStatus.IN_PROGRESS -> Color(0xFF8B5CF6)
    }

    val statusLabel = when (status) {
        OrderStatus.COMPLETED -> "Entregado"
        OrderStatus.PENDING -> "Pendiente"
        OrderStatus.CANCELLED -> "Cancelado"
        OrderStatus.CONFIRMED -> "Confirmado"
        OrderStatus.IN_PROGRESS -> "En camino"
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(statusColor))
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text("Estado", fontSize = 12.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
                    Text(statusLabel, fontWeight = FontWeight.ExtraBold, color = statusColor, fontSize = 15.sp)
                }
            }
            if (showEdit) {
                FilledTonalButton(
                    onClick = onEditClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFFE0E7FF),
                        contentColor = Color(0xFF455E91)
                    )
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Cambiar Estado", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cambiar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun InfoSection(title: String, icon: ImageVector, content: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E7FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF455E91), modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(title, fontSize = 12.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(2.dp))
                Text(content, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1B4B), lineHeight = 20.sp)
            }
        }
    }
}

@Composable
fun OrderItemRow(item: OrderItem) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E7FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color(0xFF455E91), modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.productName ?: "Producto #${item.productId}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1E1B4B)
                )
                Text(
                    "${item.quantity} x $${String.format(Locale.US, "%.2f", item.unitPrice)}",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            }
            Text(
                "$${String.format(Locale.US, "%.2f", item.quantity * item.unitPrice)}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Color(0xFF1E1B4B)
            )
        }
    }
}

@Composable
fun StatusUpdateDialog(currentStatus: OrderStatus, onDismiss: () -> Unit, onConfirm: (OrderStatus) -> Unit) {
    val statuses = OrderStatus.entries
    val statusLabels = mapOf(
        OrderStatus.PENDING to "Pendiente",
        OrderStatus.CONFIRMED to "Confirmado",
        OrderStatus.IN_PROGRESS to "En camino",
        OrderStatus.COMPLETED to "Entregado",
        OrderStatus.CANCELLED to "Cancelado"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Actualizar Estado",
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E1B4B)
            )
        },
        text = {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                statuses.forEach { status ->
                    val isSelected = status == currentStatus
                    val statusColor = when (status) {
                        OrderStatus.COMPLETED -> Color(0xFF10B981)
                        OrderStatus.PENDING -> Color(0xFFFBBF24)
                        OrderStatus.CANCELLED -> Color(0xFFEF4444)
                        OrderStatus.CONFIRMED -> Color(0xFF3B82F6)
                        OrderStatus.IN_PROGRESS -> Color(0xFF8B5CF6)
                    }

                    Surface(
                        onClick = { onConfirm(status) },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) statusColor.copy(alpha = 0.1f) else Color.Transparent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onConfirm(status) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = statusColor
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(statusColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = statusLabels[status] ?: status.apiValue,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) statusColor else Color(0xFF64748B)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}

@Composable
fun DriverAssignmentDialog(
    drivers: List<RepartidorDto>,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Asignar Repartidor",
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E1B4B)
            )
        },
        text = {
            if (drivers.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No hay repartidores disponibles.", color = Color(0xFF64748B), fontSize = 14.sp)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)) {
                    items(drivers) { driver ->
                        Surface(
                            onClick = { onConfirm(driver.idRepartidor) },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE0E7FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF455E91), modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("${driver.name} ${driver.lastname}", fontWeight = FontWeight.Bold, color = Color(0xFF1E1B4B))
                                    if (driver.vehicle != null) {
                                        Text("${driver.vehicle} - ${driver.plates}", fontSize = 12.sp, color = Color(0xFF94A3B8))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}

@Preview(showBackground = true)
@Composable
fun OrderDetailPreview() {
    val dummyOrder = Order(
        id = 2,
        userId = 1,
        orderDate = "2026-02-25T01:36:33.000Z",
        status = OrderStatus.PENDING,
        totalAmount = 150.50,
        description = "Pedido urgente",
        address = "Av. Siempre Viva 123",
        paymentType = "tarjeta",
        last4 = "1234",
        products = listOf(
            OrderItem(1, "Sensor Ultrasónico HC-SR04", 1, 50.50),
            OrderItem(8, "Módulo Wi-Fi ESP32", 2, 50.00)
        )
    )
    OrderDetailContent(
        uiState = OrderDetailUiState(order = dummyOrder),
        isAdmin = true,
        availableDrivers = emptyList(),
        onBackClick = {},
        onStatusChange = {},
        onAssignDriver = {}
    )
}
