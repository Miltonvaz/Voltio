package com.miltonvaz.voltio_1.features.products.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminProductCard
import com.miltonvaz.voltio_1.features.products.presentation.screens.UiState.MenuUiState
import com.miltonvaz.voltio_1.features.products.presentation.viewmodel.MenuViewModel
import java.util.Locale

@Composable
fun MenuScreen(
    onNavigateToOrders: () -> Unit,
    onNavigateToStock: () -> Unit,
    onNavigateToInventory: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MenuScreenContent(
        uiState = uiState,
        onNavigateToOrders = onNavigateToOrders,
        onNavigateToStock = onNavigateToStock,
        onNavigateToInventory = onNavigateToInventory
    )
}

@Composable
fun MenuScreenContent(
    uiState: MenuUiState,
    onNavigateToOrders: () -> Unit,
    onNavigateToStock: () -> Unit,
    onNavigateToInventory: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    modifier = Modifier.height(80.dp)
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Inventory2, null) },
                        label = { Text("Almacén") },
                        selected = true,
                        onClick = onNavigateToInventory,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4F46E5),
                            indicatorColor = Color(0xFFE0E7FF),
                            unselectedIconColor = Color(0xFF94A3B8)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.ListAlt, null) },
                        label = { Text("Pedidos") },
                        selected = false,
                        onClick = onNavigateToOrders,
                        colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color(0xFF94A3B8))
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.BarChart, null) },
                        label = { Text("Stock") },
                        selected = false,
                        onClick = onNavigateToStock,
                        colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color(0xFF94A3B8))
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                        .background(Brush.verticalGradient(listOf(Color(0xFFE0E7FF), Color(0xFFC7D2FE))))
                        .padding(top = 16.dp, bottom = 60.dp)
                ) {
                    AdminHeader(title = "Voltio Lab", subtitle = "Resumen de operaciones")
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .offset(y = (-30).dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    val ordersValue = uiState.totalOrdersToday.toString()
                    
                    StatCard(
                        label = "Pedidos Hoy",
                        value = ordersValue,
                        icon = Icons.AutoMirrored.Filled.ListAlt,
                        color = Color(0xFF818CF8),
                        modifier = Modifier.weight(1f)
                    )

                    val stockValueFormatted = String.format(Locale.US, "$%.2f", uiState.totalStockValue)
                    
                    StatCard(
                        label = "Valor Stock",
                        value = stockValueFormatted,
                        icon = Icons.Default.TrendingUp,
                        color = Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Text(
                    "Productos con poco stock",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                )
            }

            if (uiState.isLoading) {
                item { LoadingState() }
            } else {
                itemsIndexed(uiState.products) { _, product ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                        AdminProductCard(product = product, onEdit = {}, onDelete = {}, onClick = {})
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E1B4B)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF4F46E5))
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    MaterialTheme {
        MenuScreenContent(
            uiState = MenuUiState(isLoading = false, products = emptyList(), totalOrdersToday = 0, totalStockValue = 1250.50),
            onNavigateToOrders = {},
            onNavigateToStock = {},
            onNavigateToInventory = {}
        )
    }
}
