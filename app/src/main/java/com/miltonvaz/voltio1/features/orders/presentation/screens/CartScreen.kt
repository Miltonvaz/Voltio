package com.miltonvaz.voltio1.features.orders.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingBag
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.miltonvaz.voltio1.features.orders.domain.entities.CartItem
import com.miltonvaz.voltio1.features.orders.presentation.screens.UiState.CartUiState
import com.miltonvaz.voltio1.features.orders.presentation.viewmodel.CartViewModel
import com.miltonvaz.voltio1.features.products.domain.entities.Product
import com.miltonvaz.voltio1.features.products.presentation.components.AdminHeader
import com.miltonvaz.voltio1.features.products.presentation.components.BottomNavBarClient
import java.util.Locale

@Composable
fun CartScreen(
    navController: NavHostController,
    onCheckoutClick: () -> Unit = {},
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    CartScreenContent(
        uiState = uiState,
        onBackClick = { navController.popBackStack() },
        onCheckoutClick = onCheckoutClick,
        onIncrease = { viewModel.increaseQuantity(it) },
        onDecrease = { viewModel.decreaseQuantity(it) },
        onRemove = { viewModel.removeItem(it) },
        bottomBar = {
            BottomNavBarClient(navController = navController, selectedIndex = 2)
        }
    )
}

@Composable
fun CartScreenContent(
    uiState: CartUiState,
    onBackClick: () -> Unit,
    onCheckoutClick: () -> Unit,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onRemove: (Int) -> Unit,
    bottomBar: @Composable () -> Unit = {}
) {
    Scaffold(
        bottomBar = bottomBar,
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AdminHeader(
                title = "Tu Carrito",
                subtitle = if (uiState.cartItems.isEmpty()) "Sin productos" else "${uiState.cartItems.size} productos seleccionados",
                onBackClick = onBackClick,
                showCart = false,
                showProfile = true
            )

            if (uiState.cartItems.isEmpty()) {
                EmptyCartState()
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
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

                val total = uiState.cartItems.sumOf { it.product.price * it.quantity }
                CartSummary(
                    total = total,
                    itemCount = uiState.cartItems.size,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto
            Surface(
                modifier = Modifier.size(85.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF5F7FA)
            ) {
                AsyncImage(
                    model = cartItem.product.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }
            
            Spacer(modifier = Modifier.width(14.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            cartItem.product.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = Color(0xFF1E293B),
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("SKU: ${cartItem.product.sku}", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    }
                    IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "$${String.format(Locale.US, "%.2f", cartItem.product.price * cartItem.quantity)}",
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF4F46E5),
                            fontSize = 17.sp
                        )
                        if (cartItem.quantity > 1) {
                            Text(
                                "$${String.format(Locale.US, "%.2f", cartItem.product.price)} c/u",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                    
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFF5F7FA)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.White,
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Remove, null, modifier = Modifier.size(14.dp), tint = Color(0xFF64748B))
                                    }
                                }
                            }
                            Text(
                                "${cartItem.quantity}",
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 10.dp),
                                fontSize = 15.sp,
                                color = Color(0xFF1E293B)
                            )
                            IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFE8EEFF),
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Add, null, modifier = Modifier.size(14.dp), tint = Color(0xFF4F46E5))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartSummary(total: Double, itemCount: Int = 0, onCheckoutClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        shadowElevation = 16.dp,
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal ($itemCount ${if (itemCount == 1) "producto" else "productos"})", color = Color(0xFF94A3B8), fontSize = 14.sp)
                Text("$${String.format(Locale.US, "%.2f", total)}", fontSize = 14.sp, color = Color(0xFF64748B))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Envío", color = Color(0xFF94A3B8), fontSize = 14.sp)
                Text("Gratis", fontSize = 14.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFF1E1B4B))
                Text("$${String.format(Locale.US, "%.2f", total)}", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1B4B))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onCheckoutClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4F46E5),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text("PROCEDER AL PAGO", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
fun EmptyCartState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.size(130.dp),
                shape = CircleShape,
                color = Color(0xFFE0E7FF).copy(alpha = 0.4f)
            ) {}
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = Color(0xFFE0E7FF)
            ) {}
            Icon(
                Icons.Outlined.ShoppingBag, null,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF4F46E5)
            )
        }
        Spacer(modifier = Modifier.height(28.dp))
        Text("Tu carrito está vacío", fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B), fontSize = 22.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Explora nuestros productos y agrega\ntus favoritos al carrito",
            color = Color(0xFF94A3B8),
            fontSize = 14.sp,
            lineHeight = 21.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    val mockProducts = listOf(
        Product(1, "SKU-001", "Arduino Uno R3", "Original", 25.50, 10, null, 1, null, ""),
        Product(2, "SKU-002", "Sensor Ultrasonico", "HC-SR04", 4.20, 50, null, 1, null, "")
    )
    val mockCartItems = listOf(
        CartItem(mockProducts[0], 2),
        CartItem(mockProducts[1], 5)
    )
    
    CartScreenContent(
        uiState = CartUiState(cartItems = mockCartItems),
        onBackClick = {},
        onCheckoutClick = {},
        onIncrease = {},
        onDecrease = {},
        onRemove = {}
    )
}

@Preview(showBackground = true)
@Composable
fun EmptyCartScreenPreview() {
    CartScreenContent(
        uiState = CartUiState(cartItems = emptyList()),
        onBackClick = {},
        onCheckoutClick = {},
        onIncrease = {},
        onDecrease = {},
        onRemove = {}
    )
}
