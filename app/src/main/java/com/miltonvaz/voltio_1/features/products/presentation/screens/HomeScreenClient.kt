package com.miltonvaz.voltio_1.features.products.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader
import com.miltonvaz.voltio_1.features.products.presentation.components.BannerCard
import com.miltonvaz.voltio_1.features.products.presentation.components.BottomNavBarClient
import com.miltonvaz.voltio_1.features.products.presentation.components.CategoryRow
import com.miltonvaz.voltio_1.features.products.presentation.components.ProductCard
import com.miltonvaz.voltio_1.features.products.presentation.components.SearchBarClient
import com.miltonvaz.voltio_1.features.products.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreenClient(
    navController: NavHostController,
    viewModel: HomeViewModel,
    onCartClick: () -> Unit = {},
    onProductClick: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = uiState.products.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        bottomBar = {
            BottomNavBarClient(
                navController = navController,
                selectedIndex = 0
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header unificado del sistema
            item {
                AdminHeader(
                    title = "Voltio Store",
                    subtitle = "Hola, Explorador",
                    onBackClick = null,
                    showProfile = true,
                    showCart = true,
                    onCartClick = onCartClick
                )
            }

            // Buscador con margen de 24dp para alineación perfecta
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    SearchBarClient(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it }
                    )
                }
            }

            // Fila de Categorías
            item {
                Spacer(modifier = Modifier.height(24.dp))
                CategoryRow(onCategoryClick = {})
            }

            // Banner promocional centrado
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    BannerCard()
                }
            }

            // Título de sugerencias
            item {
                Spacer(modifier = Modifier.height(28.dp))
                Text(
                    text = "Sugerencias para ti",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Cuadrícula de productos
            if (uiState.isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF4F46E5))
                    }
                }
            } else {
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        filteredProducts.chunked(2).forEach { rowProducts ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                rowProducts.forEach { product ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        ProductCard(
                                            product = product,
                                            onClick = { onProductClick(product.id) }
                                        )
                                    }
                                }
                                if (rowProducts.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
