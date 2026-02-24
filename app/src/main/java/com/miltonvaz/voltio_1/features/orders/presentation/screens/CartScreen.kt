package com.miltonvaz.voltio_1.features.orders.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.miltonvaz.voltio_1.R
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.CartItem
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.CartUiState
import com.miltonvaz.voltio_1.features.orders.presentation.viewmodel.CartViewModel
import java.util.Locale

@Composable
fun CartScreen(
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {},
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CartScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onProfileClick = onProfileClick,
        onCheckoutClick = onCheckoutClick,
        onIncrease = { viewModel.increaseQuantity(it) },
        onDecrease = { viewModel.decreaseQuantity(it) },
        onRemove = { viewModel.removeItem(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenContent(
    uiState: CartUiState,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCheckoutClick: () -> Unit,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onRemove: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(Color(0xFFDDE7FF))
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Image(
                        painter = painterResource(id = R.drawable.voltio),
                        contentDescription = "Logo",
                        modifier = Modifier.size(40.dp)
                    )
                    IconButton(onClick = onProfileClick) {
                        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Gray))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Buscar...", color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text("Productos", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("${uiState.cartItems.size} Productos - Carrito", color = Color.Gray, fontSize = 16.sp)
                    }
                    Icon(
                        Icons.Outlined.ShoppingCart,
                        contentDescription = "Cart",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        bottomBar = {
            Column {
                if (uiState.cartItems.isNotEmpty()) {
                    val total = uiState.cartItems.sumOf { it.product.price * it.quantity }
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shadowElevation = 8.dp,
                        color = Color.White
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text("$${String.format(Locale.US, "%.2f", total)}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onCheckoutClick,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
                            ) {
                                Text("PAGAR AHORA", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                
                NavigationBar(
                    containerColor = Color(0xFFDDE7FF),
                    tonalElevation = 8.dp,
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                ) {
                    NavigationBarItem(icon = { Icon(Icons.Default.Inventory2, null) }, label = { Text("Almacén") }, selected = false, onClick = {})
                    NavigationBarItem(icon = { Icon(Icons.Default.ListAlt, null) }, label = { Text("Pedidos") }, selected = false, onClick = {})
                    NavigationBarItem(icon = { Icon(Icons.Default.BarChart, null) }, label = { Text("Stock") }, selected = false, onClick = {})
                }
            }
        }
    ) { paddingValues ->
        if (uiState.cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.cartItems) { cartItem ->
                    CartItemCard(
                        cartItem = cartItem,
                        onIncrease = { onIncrease(cartItem.product.id) },
                        onDecrease = { onDecrease(cartItem.product.id) },
                        onRemove = { onRemove(cartItem.product.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    val product = cartItem.product
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("SKU: ${product.sku}", fontSize = 10.sp, color = Color.Gray)
                    }
                    IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(20.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    product.description,
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 12.sp,
                    color = Color.DarkGray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.height(32.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray),
                        color = Color.White
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onDecrease, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Remove, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                            }
                            Text("${cartItem.quantity}", modifier = Modifier.padding(horizontal = 8.dp), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            IconButton(onClick = onIncrease, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp), tint = Color(0xFF4F46E5))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    CartScreenContent(
        uiState = CartUiState(cartItems = emptyList()),
        onBackClick = {},
        onProfileClick = {},
        onCheckoutClick = {},
        onIncrease = {},
        onDecrease = {},
        onRemove = {}
    )
}
