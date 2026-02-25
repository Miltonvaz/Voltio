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
        item { TopBarClient(onFavoriteClick = onFavoriteClick, onCartClick = onCartClick) }

        item {
            Spacer(modifier = Modifier.height(4.dp))
            SearchBarClient(query = searchQuery, onQueryChange = { searchQuery = it })
        }

        item { LocationRow(location = "Suchiapa, Chiapas") }

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
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1E293B))
                }
            } else {
                val chunked = filteredProducts.chunked(2)
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    chunked.forEach { rowItems ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            rowItems.forEach { product ->
                                Box(modifier = Modifier.weight(1f)) {
                                    ProductGridItem(
                                        product = product,
                                        onFavoriteClick = {},
                                        onCartClick = {},
                                        onClick = { onProductClick(product.id) }
                                    )
                                }
                            }
                            if (rowItems.size == 1) Box(modifier = Modifier.weight(1f))
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
            item { TopBarClient(onFavoriteClick = {}, onCartClick = {}) }
            item {
                Spacer(modifier = Modifier.height(4.dp))
                SearchBarClient(query = "", onQueryChange = {})
            }
            item { LocationRow(location = "Suchiapa, Chiapas") }
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