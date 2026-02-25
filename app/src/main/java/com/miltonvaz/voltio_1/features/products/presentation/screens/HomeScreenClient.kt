package com.miltonvaz.voltio_1.features.products.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio_1.features.products.presentation.components.*
import com.miltonvaz.voltio_1.features.products.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreenClient(
    viewModel: HomeViewModel,
    onFavoriteClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onProductClick: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = uiState.products.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFF)),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // ── Sección azul superior ──────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(Color(0xFFDDE8FF))
                    .padding(bottom = 16.dp)
            ) {
                Column {
                    TopBarClient(
                        onFavoriteClick = onFavoriteClick,
                        onCartClick = onCartClick
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    SearchBarClient(query = searchQuery, onQueryChange = { searchQuery = it })
                    Spacer(modifier = Modifier.height(4.dp))
                    LocationRow(location = "Suchiapa, Chiapas")
                }
            }
        }

        // ── Categorías ─────────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(8.dp))
            CategoryRow(onCategoryClick = {})
        }

        // ── Banner ─────────────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(8.dp))
            BannerCard()
        }

        // ── Sugerencias título ─────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sugerencias para ti",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // ── Grid productos horizontal ──────────────────────────────
        item {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1E293B))
                }
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredProducts) { product ->
                        Box(modifier = Modifier.width(160.dp)) {
                            ProductGridItem(
                                product = product,
                                onFavoriteClick = {},
                                onCartClick = {},
                                onClick = { onProductClick(product.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "HomeScreenClient", showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenClientPreview() {
    MaterialTheme {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFF)),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                        .background(Color(0xFFDDE8FF))
                        .padding(bottom = 16.dp)
                ) {
                    Column {
                        TopBarClient(onFavoriteClick = {}, onCartClick = {})
                        Spacer(modifier = Modifier.height(4.dp))
                        SearchBarClient(query = "", onQueryChange = {})
                        Spacer(modifier = Modifier.height(4.dp))
                        LocationRow(location = "Suchiapa, Chiapas")
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                CategoryRow(onCategoryClick = {})
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                BannerCard()
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Sugerencias para ti",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}