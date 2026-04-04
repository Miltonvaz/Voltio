package com.miltonvaz.voltio1.features.products.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.miltonvaz.voltio1.features.products.domain.entities.Product
import com.miltonvaz.voltio1.features.products.presentation.components.AdminHeader
import com.miltonvaz.voltio1.features.products.presentation.components.BottomNavBarAdmin
import com.miltonvaz.voltio1.features.products.presentation.screens.UiState.MenuUiState
import com.miltonvaz.voltio1.features.products.presentation.viewmodel.MenuViewModel
import java.util.Locale

@Composable
fun MenuScreen(
    navController: NavHostController,
    onNavigateToOrders: () -> Unit,
    onNavigateToStock: () -> Unit,
    onNavigateToInventory: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MenuScreenContent(
        navController = navController,
        uiState = uiState,
        onNavigateToOrders = onNavigateToOrders,
        onNavigateToStock = onNavigateToStock,
        onNavigateToInventory = onNavigateToInventory
    )
}

@Composable
fun MenuScreenContent(
    navController: NavHostController,
    uiState: MenuUiState,
    onNavigateToOrders: () -> Unit,
    onNavigateToStock: () -> Unit,
    onNavigateToInventory: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        bottomBar = {
            BottomNavBarAdmin(navController = navController, selectedIndex = 0)
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
                        .padding(bottom = 36.dp)
                ) {
                    AdminHeader(title = "Voltio Lab", subtitle = "Resumen de operaciones")
                }
            }

            // Stats row with 3 cards overlapping header
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .offset(y = (-30).dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        label = "Pedidos Hoy",
                        value = uiState.totalOrdersToday.toString(),
                        icon = Icons.AutoMirrored.Filled.ListAlt,
                        color = Color(0xFF818CF8),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Productos",
                        value = uiState.totalProducts.toString(),
                        icon = Icons.Default.Inventory2,
                        color = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Valor Stock",
                        value = "$${String.format(Locale.US, "%.0f", uiState.totalStockValue)}",
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        color = Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Low stock section
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFEE2E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Stock Crítico",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            "Productos con 5 unidades o menos",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            if (uiState.isLoading) {
                item {
                    MenuLoadingState()
                }
            } else if (uiState.products.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFF0FDF4)
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Todo en orden — sin productos con stock crítico.",
                                color = Color(0xFF166534),
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            } else {
                itemsIndexed(uiState.products) { index, product ->
                    AnimatedLowStockCard(product = product, index = index)
                }
            }
        }
    }
}

@Composable
fun AnimatedLowStockCard(product: Product, index: Int) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { 40 * (index + 1) },
            animationSpec = tween(350, easing = FastOutSlowInEasing)
        ) + fadeIn(tween(350))
    ) {
        SimpleStockCard(product)
    }
}

@Composable
fun SimpleStockCard(product: Product) {
    val stockPercent = (product.stock / 5f).coerceIn(0f, 1f)
    val stockColor = when {
        product.stock <= 2 -> Color(0xFFEF4444)
        product.stock <= 5 -> Color(0xFFF59E0B)
        else -> Color(0xFF10B981)
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F5F9)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1E1B4B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text("SKU: ${product.sku}", fontSize = 11.sp, color = Color.Gray)
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = stockColor.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, null, tint = stockColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${product.stock}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = stockColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Stock level bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFF1F5F9))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(stockPercent)
                        .clip(RoundedCornerShape(3.dp))
                        .background(stockColor)
                )
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E1B4B),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = label, fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun MenuLoadingState() {
    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF4F46E5))
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    MaterialTheme {
        MenuScreenContent(
            navController = rememberNavController(),
            uiState = MenuUiState(isLoading = false, products = emptyList(), totalOrdersToday = 0, totalStockValue = 1250.50),
            onNavigateToOrders = {},
            onNavigateToStock = {},
            onNavigateToInventory = {}
        )
    }
}
