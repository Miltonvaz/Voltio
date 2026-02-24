package com.miltonvaz.voltio_1.features.products.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminProductCard
import com.miltonvaz.voltio_1.features.products.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddProduct: () -> Unit,
    onEditProduct: (Int) -> Unit,
    onProductClick: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddProduct,
                containerColor = Color(0xFFA0BBF8),
                contentColor = Color.Companion.Black,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Nuevo Producto", modifier = Modifier.Companion.padding(start = 8.dp))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(
                        brush = Brush.Companion.verticalGradient(
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
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .height(56.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        color = Color.Companion.White,
                        shadowElevation = 8.dp
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    "Buscar componentes...",
                                    color = Color.Companion.LightGray
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Color(0xFF4F46E5)
                                )
                            },
                            modifier = Modifier.Companion.fillMaxSize(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Companion.White,
                                unfocusedContainerColor = Color.Companion.White,
                                focusedIndicatorColor = Color.Companion.Transparent,
                                unfocusedIndicatorColor = Color.Companion.Transparent
                            ),
                            singleLine = true
                        )
                    }
                }
            }

            Box(modifier = Modifier.Companion.fillMaxSize()) {
                if (uiState.isLoading) {
                    LoadingState()
                } else if (uiState.products.isEmpty()) {
                    EmptyState()
                } else {
                    val filteredProducts = uiState.products.filter {
                        it.name.contains(searchQuery, ignoreCase = true) || it.sku.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.Companion.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 24.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 100.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = "Categorías principales",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Companion.Bold,
                                color = Color(0xFF1E293B),
                                modifier = Modifier.Companion.padding(start = 4.dp, bottom = 8.dp)
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
    Box(
        modifier = Modifier.Companion.fillMaxSize(),
        contentAlignment = Alignment.Companion.Center
    ) {
        CircularProgressIndicator(color = Color(0xFF0F172A), strokeWidth = 4.dp)
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.Companion.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Inventory2,
            null,
            modifier = Modifier.Companion.size(64.dp),
            tint = Color(0xFFCBD5E1)
        )
        Text("Inventario vacío", fontWeight = FontWeight.Companion.Bold, color = Color(0xFF64748B))
    }
}


@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        // Pasamos lambdas vacías para los eventos de navegación
        HomeScreen(
            onAddProduct = {},
            onEditProduct = { id -> },
            onProductClick = { id -> }
            // Nota: El viewModel se omitirá o fallará si Hilt no está configurado
            // Lo ideal es envolver el hiltViewModel en un condicional o usar State Hoisting
        )
    }
}