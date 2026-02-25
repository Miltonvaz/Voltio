package com.miltonvaz.voltio_1.features.products.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.miltonvaz.voltio_1.features.products.domain.entities.Product
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader
import com.miltonvaz.voltio_1.features.products.presentation.screens.UiState.HomeUiState
import com.miltonvaz.voltio_1.features.products.presentation.viewmodel.HomeViewModel

@Composable
fun StockScreen(
    onBackClick: () -> Unit = {},
    onAddProduct: () -> Unit = {},
    onEditProduct: (Int) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StockScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onAddProduct = onAddProduct,
        onEditProduct = onEditProduct,
        onDeleteProduct = { viewModel.deleteProduct(it) }
    )
}

@Composable
fun StockScreenContent(
    uiState: HomeUiState,
    onBackClick: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: (Int) -> Unit,
    onDeleteProduct: (Int) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddProduct,
                containerColor = Color(0xFFA0BBF8),
                contentColor = Color.Black,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Nuevo Producto", modifier = Modifier.padding(start = 8.dp))
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
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFE0E7FF), Color(0xFFC7D2FE))
                        )
                    )
                    .padding(bottom = 32.dp)
            ) {
                Column {
                    IconButton(onClick = onBackClick, modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color(0xFF1E1B4B))
                    }
                    AdminHeader(
                        title = "Control de Stock",
                        subtitle = "Gestiona tu inventario en tiempo real"
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    LoadingState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = "Niveles de Inventario",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                            )
                        }

                        items(uiState.products) { product ->
                            StockItemCard(
                                product = product,
                                onEdit = { onEditProduct(product.id) },
                                onDelete = { onDeleteProduct(product.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StockItemCard(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val isLowStock = product.stock < 10
    val stockColor = if (isLowStock) Color(0xFFEF4444) else Color(0xFF10B981)

    Card(
        modifier = Modifier.fillMaxWidth(),
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
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1E1B4B)
                    )
                    Text(
                        text = "SKU: ${product.sku}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isLowStock) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = stockColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = "${product.stock}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = stockColor
                        )
                    }
                    Text(
                        text = "unidades",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }

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
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF4F46E5))
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

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF0F172A), strokeWidth = 4.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun StockScreenPreview() {
    val dummyProducts = listOf(
        Product(1, "A001", "Arduino Uno", "Desc", 25.0, 5, null, 1, "2023-10-10"),
        Product(2, "A002", "Sensor Ultrasonico", "Desc", 5.0, 50, null, 1, "2023-10-10")
    )
    StockScreenContent(
        uiState = HomeUiState(products = dummyProducts, isLoading = false),
        onBackClick = {},
        onAddProduct = {},
        onEditProduct = {},
        onDeleteProduct = {}
    )
}
