package com.miltonvaz.voltio1.features.products.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.miltonvaz.voltio1.features.products.presentation.screens.UiState.HomeUiState
import com.miltonvaz.voltio1.features.products.presentation.viewmodel.HomeViewModel

@Composable
fun StockScreen(
    navController: NavHostController,
    isAdmin: Boolean = true,
    onBackClick: () -> Unit = {},
    onAddProduct: () -> Unit = {},
    onEditProduct: (Int) -> Unit = {},
    onProductClick: (Int) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    StockScreenContent(
        navController = navController,
        uiState = uiState,
        isAdmin = isAdmin,
        onBackClick = onBackClick,
        onAddProduct = onAddProduct,
        onEditProduct = onEditProduct,
        onProductClick = onProductClick,
        onDeleteProduct = { viewModel.deleteProduct(it) }
    )
}

@Composable
fun StockScreenContent(
    navController: NavHostController,
    uiState: HomeUiState,
    isAdmin: Boolean,
    onBackClick: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: (Int) -> Unit,
    onProductClick: (Int) -> Unit,
    onDeleteProduct: (Int) -> Unit
) {
    val criticalCount = uiState.products.count { it.stock < 5 }
    val lowCount = uiState.products.count { it.stock in 5..9 }
    val healthyCount = uiState.products.count { it.stock >= 10 }

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        bottomBar = {
            if (isAdmin) {
                BottomNavBarAdmin(navController = navController, selectedIndex = 2)
            }
        },
        floatingActionButton = {
            if (isAdmin) {
                ExtendedFloatingActionButton(
                    onClick = onAddProduct,
                    containerColor = Color(0xFF455E91),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nuevo", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AdminHeader(
                title = "Control de Stock",
                subtitle = if (isAdmin) "Gestiona tu inventario en tiempo real" else "Consulta disponibilidad",
                onBackClick = onBackClick,
                showProfile = true,
                showCart = false
            )

            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    StockLoadingState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 8.dp, start = 20.dp, end = 20.dp, bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Stock Health Summary
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                StockHealthChip(
                                    label = "Crítico",
                                    count = criticalCount,
                                    color = Color(0xFFEF4444),
                                    modifier = Modifier.weight(1f)
                                )
                                StockHealthChip(
                                    label = "Bajo",
                                    count = lowCount,
                                    color = Color(0xFFF59E0B),
                                    modifier = Modifier.weight(1f)
                                )
                                StockHealthChip(
                                    label = "OK",
                                    count = healthyCount,
                                    color = Color(0xFF10B981),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Niveles de Inventario",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1A1C2E),
                                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                            )
                        }

                        itemsIndexed(uiState.products) { index, product ->
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) { visible = true }

                            androidx.compose.animation.AnimatedVisibility(
                                visible = visible,
                                enter = slideInVertically(
                                    initialOffsetY = { 40 * (index + 1) },
                                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                                ) + fadeIn(tween(300))
                            ) {
                                StockItemCard(
                                    product = product,
                                    showActions = isAdmin,
                                    onEdit = { onEditProduct(product.id) },
                                    onDelete = { onDeleteProduct(product.id) },
                                    onClick = { onProductClick(product.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StockHealthChip(label: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$count",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = color
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = color.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun StockItemCard(
    product: Product,
    showActions: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val isLowStock = product.stock < 10
    val isCritical = product.stock < 5
    val stockColor = when {
        isCritical -> Color(0xFFEF4444)
        isLowStock -> Color(0xFFF59E0B)
        else -> Color(0xFF10B981)
    }
    val maxStock = 100f
    val stockPercent = (product.stock / maxStock).coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                        .size(60.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF1F5F9)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1C2E),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "SKU: ${product.sku}",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }

                // Stock badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = stockColor.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isLowStock) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = stockColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${product.stock}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = stockColor
                            )
                            Text(
                                text = "unidades",
                                fontSize = 9.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }

            // Stock level bar
            Spacer(modifier = Modifier.height(12.dp))
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

            if (showActions) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 0.5.dp,
                    color = Color(0xFFE2E8F0)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onEdit,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF455E91))
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFF87171))
                    }
                }
            }
        }
    }
}

@Composable
private fun StockLoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF455E91), strokeWidth = 4.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun StockScreenPreview() {
    val dummyProducts = listOf(
        Product(1, "A001", "Arduino Uno", "Desc", 25.0, 5, null, 1, null, "2023-10-10"),
        Product(2, "A002", "Sensor Ultrasonico", "Desc", 5.0, 50, null, 1, null, "2023-10-10")
    )
    StockScreenContent(
        navController = rememberNavController(),
        uiState = HomeUiState(products = dummyProducts, isLoading = false),
        isAdmin = true,
        onBackClick = {},
        onAddProduct = {},
        onEditProduct = {},
        onProductClick = {},
        onDeleteProduct = {}
    )
}
