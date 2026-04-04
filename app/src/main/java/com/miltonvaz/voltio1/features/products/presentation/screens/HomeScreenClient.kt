package com.miltonvaz.voltio1.features.products.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.miltonvaz.voltio1.features.orders.presentation.viewmodel.CartViewModel
import com.miltonvaz.voltio1.features.products.presentation.components.*
import com.miltonvaz.voltio1.features.products.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreenClient(
    navController: NavHostController,
    viewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    onCartClick: () -> Unit = {},
    onProductClick: (Int) -> Unit = {},
    onCompanyClick: (Int) -> Unit = {}
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
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Header con gradiente suave
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFFE8EEFF), Color(0xFFF0F4FF))
                            )
                        )
                        .padding(bottom = 24.dp)
                ) {
                    Column {
                        AdminHeader(
                            title = "Voltio Store",
                            subtitle = "Hola, Explorador",
                            onBackClick = null,
                            showProfile = true,
                            showCart = true,
                            onCartClick = onCartClick
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                            SearchBarClient(
                                query = searchQuery,
                                onQueryChange = { searchQuery = it }
                            )
                        }
                    }
                }
            }

            // Categorías
            item {
                Spacer(modifier = Modifier.height(20.dp))
                CategoryRow(onCategoryClick = {})
            }

            // Banner promocional
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    BannerCard()
                }
            }

            // Sección de productos
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sugerencias para ti",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E293B)
                    )
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFE8EEFF)
                    ) {
                        Text(
                            text = "${filteredProducts.size} items",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4F46E5)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val infiniteTransition = rememberInfiniteTransition(label = "loading")
                        val scale by infiniteTransition.animateFloat(
                            initialValue = 0.8f,
                            targetValue = 1.2f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            ), label = "scale"
                        )
                        CircularProgressIndicator(
                            modifier = Modifier.scale(scale),
                            color = Color(0xFF4F46E5),
                            strokeWidth = 4.dp
                        )
                    }
                }
            } else {
                val chunkedProducts = filteredProducts.chunked(2)
                itemsIndexed(chunkedProducts) { rowIndex, rowProducts ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowProducts.forEachIndexed { colIndex, product ->
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) { visible = true }
                            
                            this.AnimatedVisibility(
                                visible = visible,
                                modifier = Modifier.weight(1f),
                                enter = slideInVertically(
                                    initialOffsetY = { 100 },
                                    animationSpec = tween(durationMillis = 500, delayMillis = (rowIndex * 100) + (colIndex * 50))
                                ) + fadeIn()
                            ) {
                                ProductCard(
                                    product = product,
                                    onClick = { onProductClick(product.id) },
                                    onAddToCart = { cartViewModel.addItem(product, 1) },
                                    onCompanyClick = { companyId -> onCompanyClick(companyId) }
                                )
                            }
                        }
                        if (rowProducts.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
