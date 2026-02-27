package com.miltonvaz.voltio_1.features.orders.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.CartItem
import com.miltonvaz.voltio_1.features.orders.presentation.viewmodel.CartViewModel
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader
import com.miltonvaz.voltio_1.features.products.presentation.components.BottomNavBarClient
import java.util.Locale

@Composable
fun CartScreen(
    navController: NavHostController,
    onCheckoutClick: () -> Unit = {},
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        bottomBar = {
            BottomNavBarClient(navController = navController, selectedIndex = 2)
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header unificado con icono de carrito
            AdminHeader(
                title = "Tu Carrito",
                subtitle = "${uiState.cartItems.size} Productos seleccionados",
                onBackClick = { navController.popBackStack() },
                showCart = true,
                showProfile = true
            )

            if (uiState.cartItems.isEmpty()) {
                EmptyCartState()
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.cartItems) { cartItem ->
                            CartItemCard(
                                cartItem = cartItem,
                                onIncrease = { viewModel.increaseQuantity(cartItem.product.id) },
                                onDecrease = { viewModel.decreaseQuantity(cartItem.product.id) },
                                onRemove = { viewModel.removeItem(cartItem.product.id) }
                            )
                        }
                    }
                }

                val total = uiState.cartItems.sumOf { it.product.price * it.quantity }
                CartSummary(
                    total = total,
                    onCheckoutClick = onCheckoutClick
                )
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = cartItem.product.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(90.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFF1F5F9)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(cartItem.product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Delete, null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                    }
                }
                Text("SKU: ${cartItem.product.sku}", fontSize = 10.sp, color = Color.Gray)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("$${String.format(Locale.US, "%.2f", cartItem.product.price)}", fontWeight = FontWeight.ExtraBold, color = Color(0xFF4F46E5), fontSize = 16.sp)
                    
                    Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFF1F5F9)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 4.dp)) {
                            IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Remove, null, modifier = Modifier.size(16.dp))
                            }
                            Text("${cartItem.quantity}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                            IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp), tint = Color(0xFF4F46E5))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartSummary(total: Double, onCheckoutClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        shadowElevation = 16.dp,
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total estimado", color = Color.Gray, fontSize = 16.sp)
                Text("$${String.format(Locale.US, "%.2f", total)}", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1B4B))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onCheckoutClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
            ) {
                Text("PROCEDER AL PAGO", fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
fun EmptyCartState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.ShoppingCart, null, modifier = Modifier.size(80.dp), tint = Color(0xFFE2E8F0))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Tu carrito está vacío", fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), fontSize = 18.sp)
    }
}
