package com.miltonvaz.voltio_1.features.products.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminProductCard
import com.miltonvaz.voltio_1.features.products.presentation.screens.UiState.MenuUiState
import com.miltonvaz.voltio_1.features.products.presentation.viewmodel.MenuViewModel


@Composable
fun MenuScreen(
    onNavigateToOrders: () -> Unit,
    onNavigateToStock: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onAddProduct: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    MenuScreenContent(
        uiState = uiState,
        onNavigateToOrders = onNavigateToOrders,
        onNavigateToStock = onNavigateToStock,
        onNavigateToInventory = onNavigateToInventory,
        onAddProduct = onAddProduct
    )
}

@Composable
fun MenuScreenContent(
    uiState: MenuUiState,
    onNavigateToOrders: () -> Unit,
    onNavigateToStock: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onAddProduct: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Inventory2, contentDescription = null) },
                    label = { Text("Almacén") },
                    selected = true,
                    onClick = onNavigateToInventory,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4F46E5),
                        indicatorColor = Color(0xFFE0E7FF)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.ListAlt, contentDescription = null) },
                    label = { Text("Pedidos") },
                    selected = false,
                    onClick = onNavigateToOrders
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("Stock") },
                    selected = false,
                    onClick = onNavigateToStock
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProduct,
                containerColor = Color(0xFF4F46E5),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(Brush.verticalGradient(listOf(Color(0xFFE0E7FF), Color(0xFFC7D2FE))))
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Voltio Lab",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E1B4B)
                    )
                    Text(
                        text = "Panel de Control",
                        fontSize = 14.sp,
                        color = Color(0xFF4338CA)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("Acceso Directo", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        MenuCard("Pedidos", Icons.Default.ShoppingCart, Color(0xFF818CF8), Modifier.weight(1f), onNavigateToOrders)
                        MenuCard("Stock Low", Icons.Default.BarChart, Color(0xFFF87171), Modifier.weight(1f), onNavigateToStock)
                    }
                }

                item {
                    Text("Actividad Reciente", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                if (uiState.isLoading) {
                    item { LoadingState() }
                } else {
                    itemsIndexed(uiState.products) { _, product ->
                        AdminProductCard(product = product, onEdit = {}, onDelete = {}, onClick = {})
                    }
                }
            }
        }
    }
}

@Composable
fun MenuCard(title: String, icon: ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(icon, contentDescription = null, tint = color)
            Text(title, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF4338CA))
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun MenuScreenPreview() {
    MaterialTheme {
        MenuScreenContent(
            uiState = MenuUiState(isLoading = false, products = emptyList()),
            onNavigateToOrders = {},
            onNavigateToStock = {},
            onNavigateToInventory = {},
            onAddProduct = {}
        )
    }
}
