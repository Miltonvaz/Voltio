package com.miltonvaz.voltio_1.features.products.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miltonvaz.voltio_1.features.products.presentation.components.*
import com.miltonvaz.voltio_1.features.products.presentation.viewmodel.HomeViewModel
import com.miltonvaz.voltio_1.features.products.presentation.viewmodel.viewModelFactory.ProductViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    factory: ProductViewModelFactory,
    onAddProduct: () -> Unit,
    onEditProduct: (Int) -> Unit,
    onProductClick: (Int) -> Unit
) {
    val viewModel: HomeViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

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
                    AdminHeader(
                        title = "Voltio Lab",
                        subtitle = "Gestión inteligente de inventario"
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Buscar componentes...", color = Color.LightGray) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF4F46E5)) },
                            modifier = Modifier.fillMaxSize(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    LoadingState()
                } else if (uiState.products.isEmpty()) {
                    EmptyState()
                } else {
                    val filteredProducts = uiState.products.filter {
                        it.name.contains(searchQuery, ignoreCase = true) || it.sku.contains(searchQuery, ignoreCase = true)
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = "Categorías principales",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                            )
                        }

                        itemsIndexed(filteredProducts) { index, product ->
                            AdminProductCard(
                                product = product,
                                onEdit = { onEditProduct(product.id) },
                                onDelete = { viewModel.deleteProduct(product.id) },
                                onClick = { onProductClick(product.id) }
                            )
                        }
                    }
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

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Inventory2, null, modifier = Modifier.size(64.dp), tint = Color(0xFFCBD5E1))
        Text("Inventario vacío", fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
    }
}