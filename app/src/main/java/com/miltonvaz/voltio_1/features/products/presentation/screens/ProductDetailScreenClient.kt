package com.miltonvaz.voltio_1.features.products.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.SettingsInputAntenna
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.miltonvaz.voltio_1.features.orders.presentation.viewmodel.CartViewModel
import com.miltonvaz.voltio_1.features.products.domain.entities.Product
import com.miltonvaz.voltio_1.features.products.presentation.components.BottomNavBarClient
import com.miltonvaz.voltio_1.features.products.presentation.components.ProductBannerClient

@Composable
fun ProductDetailScreenClient(
    navController: NavHostController,
    product: Product,
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    var quantity by remember { mutableStateOf(1) }
    val cartViewModel: CartViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            BottomNavBarClient(
                navController = navController,
                selectedIndex = 0
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFE0E7FF), Color(0xFFC7D2FE))
                        )
                    )
                    .padding(top = 16.dp, bottom = 24.dp, start = 8.dp, end = 24.dp)
            ) {
                Column {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color(0xFF1E1B4B))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = product.name,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1B4B)
                            )
                            Text(
                                text = "Detalles del componente",
                                fontSize = 14.sp,
                                color = Color(0xFF4F46E5)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Memory,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = Color(0xFF4F46E5).copy(alpha = 0.2f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                ProductBannerClient(product = product)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailCategoryItemClient("M-Control", Icons.Default.Memory, true)
                    DetailCategoryItemClient("Sensores", Icons.Default.SettingsInputAntenna, false)
                    DetailCategoryItemClient("Componentes", Icons.Default.ToggleOn, false)
                    DetailCategoryItemClient("Robótica", Icons.Default.PrecisionManufacturing, false)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Descripción",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = product.description,
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    color = Color(0xFF4A5568)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            IconButton(onClick = { if (quantity > 1) quantity-- }, modifier = Modifier.size(40.dp)) {
                                Text("−", fontSize = 22.sp, color = Color(0xFF1A1C2E), fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "$quantity",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1C2E),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            IconButton(onClick = { quantity++ }, modifier = Modifier.size(40.dp)) {
                                Text("+", fontSize = 22.sp, color = Color(0xFF1A1C2E), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            cartViewModel.addItem(product, quantity)
                            onNavigateToCart()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(56.dp).weight(1f).padding(start = 16.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Añadir", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun DetailCategoryItemClient(label: String, icon: ImageVector, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSelected) Color(0xFFE0E7FF) else Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF4F46E5) else Color.LightGray,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(text = label, fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(top = 6.dp), fontWeight = FontWeight.Medium)
    }
}
